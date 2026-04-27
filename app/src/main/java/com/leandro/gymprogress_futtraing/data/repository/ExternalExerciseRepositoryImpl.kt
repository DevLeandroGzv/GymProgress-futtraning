package com.leandro.gymprogress_futtraing.data.repository

import com.leandro.gymprogress_futtraing.data.remote.ExerciseApiService
import com.leandro.gymprogress_futtraing.data.remote.ExerciseDto
import com.leandro.gymprogress_futtraing.domain.repository.ExternalExerciseRepository
import javax.inject.Inject

class ExternalExerciseRepositoryImpl @Inject constructor(
    private val apiService: ExerciseApiService
) : ExternalExerciseRepository {

    override suspend fun getExercisesByMuscle(muscle: String): Result<List<ExerciseDto>> {
        return try {
            val response = apiService.searchExercises(muscle = muscle)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}