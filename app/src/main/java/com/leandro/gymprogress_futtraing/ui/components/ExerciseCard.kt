package com.leandro.gymprogress_futtraing.ui.components

import android.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.leandro.gymprogress_futtraing.core.saveImageToInternalStorage
import com.leandro.gymprogress_futtraing.domain.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: Exercise,
    onDelete: (Exercise) -> Unit,
    onUpdate: (Exercise) -> Unit
) {
    var weightText by remember { mutableStateOf(exercise.weight.toString()) }
    var repsText by remember { mutableStateOf(exercise.reps.toString()) }
    var nameText by remember { mutableStateOf(exercise.name) }
    val hasChanges = remember(weightText, repsText, nameText, exercise) {
        val weightDiff = weightText.toDoubleOrNull() != exercise.weight
        val repsDiff = repsText.toIntOrNull() != exercise.reps
        val nameDiff = nameText.trim() != exercise.name.trim()

        (weightDiff || repsDiff || nameDiff) && nameText.isNotBlank()
    }
    val context = LocalContext.current
    var isImageExpanded by remember { mutableStateOf(false) }
    val exercisePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {

            val internalPath = saveImageToInternalStorage(context, it)
            if (internalPath != null) {
                onUpdate(exercise.copy(imageUrl = internalPath))
            }
        }
    }

    LaunchedEffect(exercise) {
        weightText = exercise.weight.toString()
        repsText = exercise.reps.toString()
        nameText = exercise.name
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. IMAGEN (Tamaño fijo, no se mueve)
            Box(
                modifier = Modifier
                    .size(50.dp) // Tamaño fijo SIEMPRE
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Un gris suave
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    .clickable {
                        if (exercise.imageUrl != null) {
                            isImageExpanded = true // Zoom si hay foto
                        } else {
                            // Elegir foto si no hay
                            exercisePhotoLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }
            ) {
                if (exercise.imageUrl != null) {
                    // Si hay foto, la ponemos
                    AsyncImage(
                        model = exercise.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // SI NO HAY FOTO, ponemos un icono por defecto
                    Icon(
                        // Puedes usar FitnessCenter o AddAPhoto
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Añadir foto",
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2. CONTENIDO CENTRAL (Usa weight(1f) para ocupar el espacio sobrante)
            Column(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    textStyle = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Kg") },
                        modifier = Modifier.width(70.dp), // Un poco más estrecho
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = { repsText = it },
                        label = { Text("Reps") },
                        modifier = Modifier.width(70.dp), // Un poco más estrecho
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            // 3. SECCIÓN DE BOTONES (Tamaño fijo a la derecha)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                // Usamos un Box animado o simplemente visibilidad para el Check
                if (hasChanges) {
                    IconButton(
                        onClick = {
                            val weight = weightText.toDoubleOrNull() ?: 0.0
                            val reps = repsText.toIntOrNull() ?: 0
                            onUpdate(exercise.copy(name = nameText.trim(),weight = weight, reps = reps))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Guardar",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }

                IconButton(onClick = { onDelete(exercise) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                if (isImageExpanded && exercise.imageUrl != null) {
                    // Usamos un Box que ocupa toda la pantalla con fondo semitransparente
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.8f))
                            .clickable { isImageExpanded = false }, // Cerrar zoom al tocar
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.size(300.dp), // Tamaño grande para el zoom
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            AsyncImage(
                                model = exercise.imageUrl,
                                contentDescription = "Zoom ejercicio",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit // Ajustar sin recortar
                            )
                        }
                    }
                }
            }
        }
    }
}