package com.leandro.gymprogress_futtraing.core

object ExerciseTranslator {
    private val muscleMap = mapOf(
        "biceps" to "Bíceps",
        "chest" to "Pecho",
        "back" to "Espalda",
        "quadriceps" to "Cuádriceps",
        "shoulders" to "Hombros",
        "abs" to "Abdominales"
    )

    fun translateMuscle(muscle: String?): String? = muscleMap[muscle?.lowercase()] ?: muscle


}