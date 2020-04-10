package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.ListRoutinesUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ListRoutineTrainerViewModel(listRoutineUseCase: ListRoutinesUseCase) : ViewModel() {
    val routineList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routineList = listRoutineUseCase.getOwnRoutines()
            emit(routineList)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}