package com.tfg.workoutagent.presentation.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.loginUseCases.LoginUseCase

class LoginViewModelFactory (private val useCase: LoginUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return modelClass.getConstructor(LoginUseCase::class.java).newInstance(useCase)
    }

}