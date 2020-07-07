package com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase

class ProfileTrainerViewModelFactory(private val darkMode: Boolean, private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileTrainerViewModel(darkMode, displayProfileUserUseCase) as T
    }
}