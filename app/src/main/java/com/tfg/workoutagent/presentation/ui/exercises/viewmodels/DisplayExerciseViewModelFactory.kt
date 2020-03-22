package com.tfg.workoutagent.presentation.ui.exercises.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.exercises.DisplayUseCase

class DisplayExerciseViewModelFactory(private val id: String, private val useCase: DisplayUseCase) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // return modelClass.getConstructor(DisplayUseCase::class.java).newInstance(useCase)
        return DisplayExerciseViewModel(id, useCase) as T
    }
}