package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.userUseCases.ListCustomerUseCase

class ListCustomerViewModelFactory(private val listCustomerUseCase: ListCustomerUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ListCustomerUseCase::class.java).newInstance(listCustomerUseCase)
    }
}