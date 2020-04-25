package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase

class EditRoutineViewModelFactory(private val routineId: String,private val manageRoutineUseCase: ManageRoutineUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditRoutineViewModel(routineId, manageRoutineUseCase) as T
    }
}