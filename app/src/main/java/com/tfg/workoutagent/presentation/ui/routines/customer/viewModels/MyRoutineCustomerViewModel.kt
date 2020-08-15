package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class MyRoutineCustomerViewModel(manageRoutineCustomerUseCase: ManageRoutineCustomerUseCase) : ViewModel() {
    val routine = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routine = manageRoutineCustomerUseCase.getAssignedRoutine()
            emit(routine)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}