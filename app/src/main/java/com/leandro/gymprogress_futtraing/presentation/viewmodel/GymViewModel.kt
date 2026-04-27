package com.leandro.gymprogress_futtraing.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
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
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GymViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val addExerciseUseCase: AddExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase,
    private val preferences: SharedPreferences


    ) : ViewModel() {

    private val _state = MutableStateFlow(GymUiState())
    val state: StateFlow<GymUiState> = _state.asStateFlow()
    var profileImageUri = mutableStateOf(preferences.getString("profile_uri", null))
        private set

    init {
        loadExercises()
    }
    fun saveProfileImage(uri: String) {
        preferences.edit().putString("profile_uri", uri).apply()
        profileImageUri.value = uri
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

    fun onUpdateExercise(updatedExercise: Exercise) {
        viewModelScope.launch {
            updateExerciseUseCase(updatedExercise)
        }
    }


}