package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListHistoricActivitiesCustomerViewModel(private val routineId: String, numDay: Int, listHistoricRoutinesUseCase: HistoricRoutinesUseCase) : ViewModel() {
    val activityList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routineResource = listHistoricRoutinesUseCase.getHistoricRoutineById(routineId)
            emit(routineResource)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}
