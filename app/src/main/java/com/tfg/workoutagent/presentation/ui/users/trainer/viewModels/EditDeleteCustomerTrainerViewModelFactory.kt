package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase

class EditDeleteCustomerTrainerViewModelFactory(private val id : String, private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditDeleteCustomerTrainerViewModel(id, manageCustomerTrainerUseCase) as T
    }
}