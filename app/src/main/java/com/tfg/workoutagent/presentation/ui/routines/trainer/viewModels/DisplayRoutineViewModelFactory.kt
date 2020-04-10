package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class DisplayRoutineViewModelFactory(private val routineId: String, private val manageRoutineUseCase: ManageRoutineUseCase) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // return modelClass.getConstructor(DisplayUseCase::class.java).newInstance(useCase)
        return DisplayRoutineViewModel(routineId, manageRoutineUseCase) as T
    }
}