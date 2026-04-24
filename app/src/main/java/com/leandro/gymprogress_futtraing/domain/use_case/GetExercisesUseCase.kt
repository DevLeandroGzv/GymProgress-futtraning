package com.leandro.gymprogress_futtraing.domain.use_case

import com.leandro.gymprogress_futtraing.domain.model.Exercise
import com.leandro.gymprogress_futtraing.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    operator fun invoke(group: String): Flow<List<Exercise>> {
        return repository.getExercisesByGroup(group)
    }
}