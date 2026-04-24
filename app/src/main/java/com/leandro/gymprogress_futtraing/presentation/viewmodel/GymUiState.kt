package com.leandro.gymprogress_futtraing.presentation.viewmodel

import com.leandro.gymprogress_futtraing.domain.model.Exercise

data class GymUiState(val userName: String = "Tu Nombre",
                      val userWeight: String = "0.0",
                      val userHeight: String = "0",
                      val legExercises: List<Exercise> = emptyList(),
                      val torsoExercises: List<Exercise> = emptyList(),
                      val isLoading: Boolean = false)
