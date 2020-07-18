package com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.goalUseCases.ManageGoalUseCase

class CreateGoalCustomerViewModelFactory(private val manageGoalUseCase : ManageGoalUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageGoalUseCase::class.java).newInstance(manageGoalUseCase)
    }
}