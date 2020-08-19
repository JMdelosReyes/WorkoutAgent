package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCase

class ListHistoricRoutinesCustomerViewModelFactory(private val customerId:String, private val historicRoutinesUseCase: HistoricRoutinesUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListHistoricRoutinesCustomerViewModel(
            customerId,
            historicRoutinesUseCase
        ) as T
    }
}