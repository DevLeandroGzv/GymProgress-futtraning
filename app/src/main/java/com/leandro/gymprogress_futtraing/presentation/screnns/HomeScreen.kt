package com.leandro.gymprogress_futtraing.presentation.screnns

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leandro.gymprogress_futtraing.presentation.viewmodel.GymViewModel
import com.leandro.gymprogress_futtraing.ui.components.ExerciseCard
import com.leandro.gymprogress_futtraing.ui.components.ProfileHeader

@Composable
fun HomeScreen(viewModel: GymViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Piernas", "Torso")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    Scaffold(
        topBar = {

            ProfileHeader(
                name = state.userName,
                weight = state.userWeight,
                height = state.userHeight,
                imageUri = selectedImageUri?.toString(), // Le pasamos la nueva imagen
                onAvatarClick = { photoPickerLauncher.launch("image/*") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {

                val group = if (selectedTabIndex == 0) "PIERNA" else "TORSO"
                viewModel.onAddExercise("Nuevo Ejercicio", group)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Listado dinámico según la pestaña seleccionada
            val currentExercises = if (selectedTabIndex == 0) state.legExercises else state.torsoExercises

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(currentExercises) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onDelete = { viewModel.onDeleteExercise(it) },
                        onUpdate = { /* Implementaremos esto luego */ }
                    )
                }
            }
        }
    }
}