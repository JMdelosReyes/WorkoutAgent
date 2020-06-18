package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.exerciseUseCases.ListExercisesUseCase

class ListExerciseViewModelFactory(private val listExerciseUseCase: ListExercisesUseCase):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ListExercisesUseCase::class.java).newInstance(listExerciseUseCase)
    }
}