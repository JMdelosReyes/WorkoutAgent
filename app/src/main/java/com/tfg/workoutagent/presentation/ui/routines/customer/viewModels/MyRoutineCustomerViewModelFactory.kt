package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCase

class MyRoutineCustomerViewModelFactory(private val manageRoutineCustomerUseCase: ManageRoutineCustomerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageRoutineCustomerUseCase::class.java).newInstance(manageRoutineCustomerUseCase)
    }
}