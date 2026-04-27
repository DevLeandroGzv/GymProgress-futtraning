package com.leandro.gymprogress_futtraing.presentation.screnns

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.input.ImeAction
import com.leandro.gymprogress_futtraing.presentation.viewmodel.GymViewModel
import com.leandro.gymprogress_futtraing.ui.components.ApiExerciseCard
import com.leandro.gymprogress_futtraing.ui.components.ExerciseCard
import com.leandro.gymprogress_futtraing.ui.components.ProfileHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: GymViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Piernas", "Torso")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var showLibrarySheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
        try {
            val contentResolver = context.contentResolver
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(it, takeFlags)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        selectedImageUri = it
        // Aquí deberías llamar a un método del ViewModel para guardar esta URI en SharedPreferences
        viewModel.saveProfileImage(it.toString())
    }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Dejamos esto vacío o ponemos una TopAppBar pequeña si quieres
        },
        floatingActionButton = { /* ... tu FAB ... */ }
    ) { paddingValues ->
        // Aplicamos el padding que viene del Scaffold
        Column(modifier = Modifier.padding(paddingValues)) {

            // 1. Ponemos el Header aquí arriba
            ProfileHeader(
                name = state.userName,
                weight = state.userWeight,
                height = state.userHeight,
                imageUri = viewModel.profileImageUri.value,
                onAvatarClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
            Button(
                onClick = { showLibrarySheet = true },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Text(" Explorar Librería Ninja")
            }
            // 2. Las pestañas debajo del Header
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            if (showLibrarySheet) {
                ModalBottomSheet(
                    onDismissRequest = { showLibrarySheet = false },
                    sheetState = rememberModalBottomSheetState()
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Text("Buscador por Músculo", style = MaterialTheme.typography.headlineSmall)

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Músculo (biceps, chest, leg...)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            // Configura el botón del teclado

                            // Define qué pasa cuando presionas esa lupa
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if(searchQuery.isNotBlank()) {
                                        viewModel.searchExternalExercises(searchQuery)
                                    }
                                }
                            ),
                            trailingIcon = {
                                IconButton(onClick = {
                                    if(searchQuery.isNotBlank()) viewModel.searchExternalExercises(searchQuery)
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                                }
                            }
                        )

                        if (viewModel.isSearching) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(viewModel.apiExercises) { apiEx ->
                                ApiExerciseCard(apiEx) {
                                    val group = if (selectedTabIndex == 0) "PIERNA" else "TORSO"
                                    viewModel.onAddExercise(apiEx.name, group)
                                    showLibrarySheet = false
                                }
                            }
                        }
                    }
                }
            }
            // Listado dinámico según la pestaña seleccionada
            val currentExercises = if (selectedTabIndex == 0) state.legExercises else state.torsoExercises

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(currentExercises,key = { it.id ?:0 }) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onDelete = { viewModel.onDeleteExercise(it) },
                        onUpdate = { updatedExercise ->
                            viewModel.onUpdateExercise(updatedExercise)
                            focusManager.clearFocus() // 1. Quita el teclado y el foco
                            scope.launch {
                                snackbarHostState.showSnackbar("Progreso guardado") // 2. Muestra el mensaje
                            } }
                    )
                }
            }
        }
    }
}

@Composable
fun SnackbarHostState() {
    TODO("Not yet implemented")
}