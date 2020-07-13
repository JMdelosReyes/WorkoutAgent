package com.tfg.workoutagent.presentation.ui.profile.customer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCase

class DeleteProfileSendEmailCustomerViewModelFactory (private val customerId : String, private val manageProfileUseCase: ManageProfileUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DeleteProfileSendEmailCustomerViewModel(customerId, manageProfileUseCase) as T
    }
}