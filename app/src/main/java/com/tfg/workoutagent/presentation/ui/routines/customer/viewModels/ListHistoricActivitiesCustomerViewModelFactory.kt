package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCase

class ListHistoricActivitiesCustomerViewModelFactory(private val routineId:String, private val dayNum : Int, private val historicRoutinesUseCase: HistoricRoutinesUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListHistoricActivitiesCustomerViewModel(
            routineId,
            dayNum,
            historicRoutinesUseCase
        ) as T

    }
}