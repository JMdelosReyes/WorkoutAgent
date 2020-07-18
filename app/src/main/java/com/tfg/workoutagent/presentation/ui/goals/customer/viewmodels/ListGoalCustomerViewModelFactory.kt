package com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerUseCase

class ListGoalCustomerViewModelFactory(private val listGoalCustomerUseCase : ListGoalCustomerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ListGoalCustomerUseCase::class.java).newInstance(listGoalCustomerUseCase)
    }
}