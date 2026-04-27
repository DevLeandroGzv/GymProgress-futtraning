package com.leandro.gymprogress_futtraing.data.repository

import com.leandro.gymprogress_futtraing.data.local.dao.ExerciseDao
import com.leandro.gymprogress_futtraing.data.mapper.toDomain
import com.leandro.gymprogress_futtraing.data.mapper.toEntity
import com.leandro.gymprogress_futtraing.data.remote.ExerciseDto
import com.leandro.gymprogress_futtraing.domain.model.Exercise
import com.leandro.gymprogress_futtraing.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExcerciseRepositoryImpl(
    private val dao: ExerciseDao,
) : ExerciseRepository {

    override fun getExercisesByGroup(group: String): Flow<List<Exercise>> {
        return dao.getExercisesByGroup(group).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertExercise(exercise: Exercise) {
        dao.insertExercise(exercise.toEntity())
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        dao.deleteExercise(exercise.toEntity())
    }

    override suspend fun updateExercise(exercise: Exercise) {
        dao.insertExercise(exercise.toEntity())
    }
}