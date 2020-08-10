package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.ListRoutinesUseCase
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListRoutineTrainerViewModel(listRoutineUseCase: ListRoutinesUseCase) : ViewModel() {

    private var generalRoutines: MutableList<Routine> = mutableListOf()

    private var assignedRoutines: MutableList<Routine> = mutableListOf()

    val routineList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routineList = listRoutineUseCase.getOwnRoutines()
            if (routineList is Resource.Success) {
                organizeRoutines(routineList.data)
            }
            emit(routineList)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    private fun organizeRoutines(routineList: MutableList<Routine>) {
        generalRoutines = mutableListOf()
        assignedRoutines = mutableListOf()
        routineList.forEach {
            if (it.customer == null) {
                generalRoutines.add(it)
            } else if (it.current) {
                assignedRoutines.add(it)
            }
        }
    }

    fun getGeneralRoutines(): MutableList<Routine> {
        return this.generalRoutines
    }

    fun getAssignedRoutines(): MutableList<Routine> {
        return this.assignedRoutines
    }
}