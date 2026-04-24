package com.leandro.gymprogress_futtraing.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandro.gymprogress_futtraing.domain.model.Exercise
import com.leandro.gymprogress_futtraing.domain.use_case.AddExerciseUseCase
import com.leandro.gymprogress_futtraing.domain.use_case.DeleteExerciseUseCase
import com.leandro.gymprogress_futtraing.domain.use_case.GetExercisesUseCase
import com.leandro.gymprogress_futtraing.domain.use_case.UpdateExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase

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
    fun onAddExercise(name: String, group: String) {
        viewModelScope.launch {
            val newExercise = Exercise(
                name = name,
                muscleGroup = group,
                weight = 0.0,
                reps = 0
            )
            addExerciseUseCase(newExercise)
        }
    }

    fun onDeleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            deleteExerciseUseCase(exercise)
        }
    }

    fun onUpdateExercise(exercise: Exercise, newWeight: Double, newReps: Int) {
        viewModelScope.launch {
            val updatedExercise = exercise.copy(weight = newWeight, reps = newReps)
            updateExerciseUseCase(updatedExercise)
        }
    }
}