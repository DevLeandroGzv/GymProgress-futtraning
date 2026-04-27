package com.leandro.gymprogress_futtraing.domain.repository

import com.leandro.gymprogress_futtraing.data.remote.ExerciseDto
import com.leandro.gymprogress_futtraing.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getExercisesByGroup(group: String): Flow<List<Exercise>>

    suspend fun insertExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)
}