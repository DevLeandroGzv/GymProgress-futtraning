package com.leandro.gymprogress_futtraing.domain.repository

import com.leandro.gymprogress_futtraing.data.remote.ExerciseDto

interface ExternalExerciseRepository {
    suspend fun getExercisesByMuscle(muscle: String): Result<List<ExerciseDto>>
}