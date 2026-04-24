package com.leandro.gymprogress_futtraing.data.mapper

import com.leandro.gymprogress_futtraing.data.local.entities.ExerciseEntity
import com.leandro.gymprogress_futtraing.domain.model.Exercise


fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(id, name, muscleGroup, weight, reps, imageUrl)
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(id ?: 0, name, muscleGroup, weight, reps, imageUrl)
}