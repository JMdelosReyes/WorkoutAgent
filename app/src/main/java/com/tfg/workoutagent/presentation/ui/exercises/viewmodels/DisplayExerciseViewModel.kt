package com.tfg.workoutagent.presentation.ui.exercises.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.exercises.DisplayUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class DisplayExerciseViewModel(private val exerciseId: String, useCase: DisplayUseCase) :
    ViewModel() {
    val getExercise = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val exercise = useCase.getExercise(exerciseId)
            emit(exercise)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}