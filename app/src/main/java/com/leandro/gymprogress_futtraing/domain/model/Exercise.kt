package com.leandro.gymprogress_futtraing.domain.model

data class Exercise(val id: Int? = null,
                    val name: String,
                    val muscleGroup: String, // Usaremos "PIERNA" o "TORSO"
                    val weight: Double,
                    val reps: Int,
                    val imageUrl: String? = null)
