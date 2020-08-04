package com.tfg.workoutagent.presentation.ui.profile.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase

class ProfileAdminViewModelFactory(private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileAdminViewModel(displayProfileUserUseCase) as T
    }

}