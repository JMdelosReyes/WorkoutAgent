package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCase

class EditDeleteExerciseViewModelFactory(
    private val id: String,
    private val manageExerciseUseCase: ManageExerciseUseCase
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditDeleteExerciseViewModel(id, manageExerciseUseCase) as T
    }
}