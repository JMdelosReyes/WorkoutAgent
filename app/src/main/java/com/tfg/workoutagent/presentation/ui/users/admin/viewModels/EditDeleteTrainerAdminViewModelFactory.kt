package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCase

class EditDeleteTrainerAdminViewModelFactory(private val id: String, private val manageTrainerAdminUseCase: ManageTrainerAdminUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditDeleteTrainerAdminViewModel(id, manageTrainerAdminUseCase) as T
    }
}