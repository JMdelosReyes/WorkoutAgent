package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.ListUserAdminUseCase

class UserListViewModelFactory(private val listUserAdminUseCase: ListUserAdminUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ListUserAdminUseCase::class.java).newInstance(listUserAdminUseCase)
    }
}