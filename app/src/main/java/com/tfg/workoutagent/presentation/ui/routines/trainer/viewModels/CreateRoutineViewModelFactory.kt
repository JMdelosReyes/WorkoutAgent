package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase

class CreateRoutineViewModelFactory(private val manageRoutineUseCase: ManageRoutineUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageRoutineUseCase::class.java)
            .newInstance(manageRoutineUseCase)
    }
}