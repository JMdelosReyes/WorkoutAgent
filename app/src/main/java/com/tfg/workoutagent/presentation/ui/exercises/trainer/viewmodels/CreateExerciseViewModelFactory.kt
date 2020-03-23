package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCase

class CreateExerciseViewModelFactory(private val manageExerciseUseCase: ManageExerciseUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageExerciseUseCase::class.java)
            .newInstance(manageExerciseUseCase)
    }
}