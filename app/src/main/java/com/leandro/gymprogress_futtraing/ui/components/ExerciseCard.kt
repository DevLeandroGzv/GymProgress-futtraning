package com.leandro.gymprogress_futtraing.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.leandro.gymprogress_futtraing.core.saveImageToInternalStorage
import com.leandro.gymprogress_futtraing.domain.model.Exercise

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onDelete: (Exercise) -> Unit,
    onUpdate: (Exercise) -> Unit
) {
    val context = LocalContext.current


    var weightText by remember(exercise.id) { mutableStateOf(exercise.weight.toString()) }
    var repsText by remember(exercise.id) { mutableStateOf(exercise.reps.toString()) }
    var nameText by remember(exercise.id) { mutableStateOf(exercise.name) }
    var isImageExpanded by remember { mutableStateOf(false) }

    val hasChanges = remember(weightText, repsText, nameText, exercise) {
        val weightDiff = weightText.toDoubleOrNull() != exercise.weight
        val repsDiff = repsText.toIntOrNull() != exercise.reps
        val nameDiff = nameText.trim() != exercise.name.trim()
        (weightDiff || repsDiff || nameDiff) && nameText.isNotBlank()
    }

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

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    .clickable {
                        if (exercise.imageUrl != null) isImageExpanded = true
                        else exercisePhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
            ) {
                if (exercise.imageUrl != null) {
                    AsyncImage(
                        model = exercise.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))


            Column(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    textStyle = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Kg", fontSize = 10.sp) },
                        modifier = Modifier.width(75.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = { repsText = it },
                        label = { Text("Reps", fontSize = 10.sp) },
                        modifier = Modifier.width(75.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                if (hasChanges) {
                    IconButton(onClick = {
                        val weight = weightText.toDoubleOrNull() ?: 0.0
                        val reps = repsText.toIntOrNull() ?: 0
                        onUpdate(exercise.copy(name = nameText.trim(), weight = weight, reps = reps))
                    }) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
                    }
                }
                IconButton(onClick = { onDelete(exercise) }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }


    if (isImageExpanded && exercise.imageUrl != null) {
        Dialog(onDismissRequest = { isImageExpanded = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        AsyncImage(
                            model = exercise.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                isImageExpanded = false
                                exercisePhotoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(" Cambiar")
                        }

                        Button(
                            onClick = {
                                isImageExpanded = false
                                onUpdate(exercise.copy(imageUrl = null))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(" Quitar")
                        }
                    }

                    TextButton(
                        onClick = { isImageExpanded = false },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}