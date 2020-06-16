package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class DisplayRoutineViewModel(private val routineId: String,manageRoutineUseCase: ManageRoutineUseCase) : ViewModel() {
    val routine = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routine = manageRoutineUseCase.getRoutine(routineId)
            emit(routine)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}