package com.tfg.workoutagent.presentation.ui.profile.customer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCase

class EditProfileCustomerViewModelFactory(private val manageProfileUseCase: ManageProfileUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageProfileUseCase::class.java).newInstance(manageProfileUseCase)
    }
}