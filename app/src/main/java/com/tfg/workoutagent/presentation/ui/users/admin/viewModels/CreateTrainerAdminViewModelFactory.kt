package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCase

class CreateTrainerAdminViewModelFactory(private val manageTrainerAdminUseCase: ManageTrainerAdminUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageTrainerAdminUseCase::class.java).newInstance(manageTrainerAdminUseCase)
    }
}