package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.DisplayCustomerTrainerUseCase

class DisplayCustomerTrainerViewModelFactory(private val id: String, private val customerTrainerUseCase: DisplayCustomerTrainerUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DisplayCustomerTrainerViewModel(id, customerTrainerUseCase) as T
    }

}