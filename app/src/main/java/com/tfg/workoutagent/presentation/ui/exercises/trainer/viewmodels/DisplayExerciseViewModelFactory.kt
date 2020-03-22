package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.exerciseUseCases.DisplayExerciseUseCase

class DisplayExerciseViewModelFactory(private val id: String, private val exerciseUseCase: DisplayExerciseUseCase) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // return modelClass.getConstructor(DisplayUseCase::class.java).newInstance(useCase)
        return DisplayExerciseViewModel(id, exerciseUseCase) as T
    }
}