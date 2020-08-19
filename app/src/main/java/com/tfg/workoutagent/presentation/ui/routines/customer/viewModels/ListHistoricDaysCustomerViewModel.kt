package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListHistoricDaysCustomerViewModel(private val routineId: String, listHistoricRoutinesUseCase: HistoricRoutinesUseCase) : ViewModel() {
    val daysList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val daysResource = listHistoricRoutinesUseCase.getHistoricRoutineById(routineId)
            emit(daysResource)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}
