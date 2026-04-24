package com.leandro.gymprogress_futtraing.domain.use_case

import com.leandro.gymprogress_futtraing.domain.model.Exercise
import com.leandro.gymprogress_futtraing.domain.repository.ExerciseRepository
import javax.inject.Inject

class DeleteExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exercise: Exercise) {
        repository.deleteExercise(exercise)
    }
}