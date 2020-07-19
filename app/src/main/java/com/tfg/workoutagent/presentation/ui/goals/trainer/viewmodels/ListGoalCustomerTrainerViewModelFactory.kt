package com.tfg.workoutagent.presentation.ui.goals.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerTrainerUseCase

class ListGoalCustomerTrainerViewModelFactory(private val id: String, private val listGoalCustomerTrainerUseCase : ListGoalCustomerTrainerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListGoalCustomerTrainerViewModel(id, listGoalCustomerTrainerUseCase) as T
    }
}