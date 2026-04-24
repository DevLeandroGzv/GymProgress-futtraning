package com.leandro.gymprogress_futtraing.ui.components

import android.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
            // Imagen redonda de la máquina/ejercicio
            AsyncImage(
                model = exercise.imageUrl ?: R.drawable.btn_default,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { newValue ->
                            // Permitimos solo números y un punto decimal
                            if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                                weightText = newValue
                                // 2. Actualizamos la base de datos si es un número válido
                                newValue.toDoubleOrNull()?.let {
                                    onUpdate(exercise.copy(weight = it))
                                }
                            }
                        },
                        label = { Text("Kg") },
                        modifier = Modifier.width(90.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Campo para Repeticiones
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                                repsText = newValue
                                // 2. Actualizamos la base de datos
                                newValue.toIntOrNull()?.let {
                                    onUpdate(exercise.copy(reps = it))
                                }
                            }
                        },
                        label = { Text("Reps") },
                        modifier = Modifier.width(90.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
        }
    }
}