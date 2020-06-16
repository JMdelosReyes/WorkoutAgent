package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.ListRoutinesUseCase

class ListRoutineTrainerViewModelFactory(private val listRoutinesUseCase: ListRoutinesUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ListRoutinesUseCase::class.java).newInstance(listRoutinesUseCase)
    }
}