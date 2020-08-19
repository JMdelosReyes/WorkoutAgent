package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListHistoricRoutinesCustomerViewModel(private val customerId: String, listHistoricRoutinesUseCase: HistoricRoutinesUseCase) : ViewModel() {

    val routineList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routineList = listHistoricRoutinesUseCase.getHistoricRoutinesByCustomerId(customerId)
            emit(routineList)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}
