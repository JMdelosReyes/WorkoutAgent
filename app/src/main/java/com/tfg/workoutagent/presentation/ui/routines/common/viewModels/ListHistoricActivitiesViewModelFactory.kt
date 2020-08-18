package com.tfg.workoutagent.presentation.ui.routines.common.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCase
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCase

class ListHistoricActivitiesViewModelFactory(private val routineId:String, private val dayNum : Int, private val historicRoutinesUseCase: HistoricRoutinesUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListHistoricActivitiesViewModel(routineId, dayNum, historicRoutinesUseCase) as T

    }
}