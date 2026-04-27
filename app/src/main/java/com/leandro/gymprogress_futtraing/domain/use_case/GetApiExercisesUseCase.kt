package com.leandro.gymprogress_futtraing.domain.use_case

import com.leandro.gymprogress_futtraing.data.remote.ExerciseDto
import com.leandro.gymprogress_futtraing.domain.repository.ExternalExerciseRepository
import javax.inject.Inject

class GetApiExercisesUseCase @Inject constructor(
    private val repository: ExternalExerciseRepository
) {
    suspend operator fun invoke(muscle: String): Result<List<ExerciseDto>> {
        return repository.getExercisesByMuscle(muscle)
    }
}