package com.leandro.gymprogress_futtraing.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leandro.gymprogress_futtraing.data.remote.ExerciseDto

@Composable
fun ApiExerciseCard(
    exercise: ExerciseDto,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(exercise.name.uppercase(), fontWeight = FontWeight.Bold)
                    Text("informacion: ${exercise.instructions}", style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = onAddClick) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Importar", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}