package com.leandro.gymprogress_futtraing.presentation.viewmodel

import com.leandro.gymprogress_futtraing.domain.model.Exercise

data class GymUiState(val userName: String = "Leandro Gonzalez",
                      val userWeight: String = "91",
                      val userHeight: String = "1.82",
                      val legExercises: List<Exercise> = emptyList(),
                      val torsoExercises: List<Exercise> = emptyList(),
                      val isLoading: Boolean = false)
