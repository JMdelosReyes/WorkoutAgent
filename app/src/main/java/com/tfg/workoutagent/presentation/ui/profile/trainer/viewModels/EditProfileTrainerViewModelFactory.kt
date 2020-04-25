package com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCase

class EditProfileTrainerViewModelFactory(private val manageProfileUseCase: ManageProfileUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileTrainerViewModel(manageProfileUseCase) as T
    }
}