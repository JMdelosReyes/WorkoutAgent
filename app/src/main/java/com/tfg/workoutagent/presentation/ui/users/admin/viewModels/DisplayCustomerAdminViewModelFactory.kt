package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerAdminUseCase

class DisplayCustomerAdminViewModelFactory(private val id: String, private val manageCustomerAdminUseCase: ManageCustomerAdminUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DisplayCustomerAdminViewModel(id, manageCustomerAdminUseCase) as T
    }
}