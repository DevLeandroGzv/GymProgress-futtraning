package com.leandro.gymprogress_futtraing.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandro.gymprogress_futtraing.domain.use_case.GetExercisesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class GymViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,

    ) : ViewModel() {

    private val _state = MutableStateFlow(GymUiState())
    val state: StateFlow<GymUiState> = _state.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {

            getExercisesUseCase("PIERNA").collect { list ->
                _state.update { it.copy(legExercises = list) }
            }
        }
        viewModelScope.launch {
            getExercisesUseCase("TORSO").collect { list ->
                _state.update { it.copy(torsoExercises = list) }
            }
        }
    }
}