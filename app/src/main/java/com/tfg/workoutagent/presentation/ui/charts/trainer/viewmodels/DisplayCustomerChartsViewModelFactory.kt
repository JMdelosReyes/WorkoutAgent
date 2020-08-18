package com.tfg.workoutagent.presentation.ui.charts.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase

class DisplayCustomerChartsViewModelFactory(private val customerId: String, private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DisplayCustomerChartsViewModel(customerId, manageCustomerTrainerUseCase) as T
        }
}