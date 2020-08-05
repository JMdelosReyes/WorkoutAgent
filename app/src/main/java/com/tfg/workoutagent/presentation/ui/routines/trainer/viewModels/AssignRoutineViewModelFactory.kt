package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.AssignRoutinesUseCase

class AssignRoutineViewModelFactory(private val assignRoutineUseCase: AssignRoutinesUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AssignRoutinesUseCase::class.java)
            .newInstance(assignRoutineUseCase)
    }
}