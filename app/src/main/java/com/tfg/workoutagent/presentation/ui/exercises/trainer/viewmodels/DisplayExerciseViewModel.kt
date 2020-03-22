package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.exerciseUseCases.DisplayExerciseUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class DisplayExerciseViewModel(private val exerciseId: String, exerciseUseCase: DisplayExerciseUseCase) :
    ViewModel() {
    val getExercise = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val exercise = exerciseUseCase.getExercise(exerciseId)
            emit(exercise)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}