package com.leandro.gymprogress_futtraing.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                          val name: String,
                          val muscleGroup: String,
                          val weight: Double,
                          val reps: Int,
                          val imageUrl: String?)
