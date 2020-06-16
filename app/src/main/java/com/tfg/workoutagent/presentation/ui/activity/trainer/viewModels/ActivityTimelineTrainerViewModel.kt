package com.tfg.workoutagent.presentation.ui.activity.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.routineUseCases.ActivityTimelineRoutinesUseCase
import com.tfg.workoutagent.domain.routineUseCases.ListRoutinesUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ActivityTimelineTrainerViewModel(activityTimelineRoutineUseCase: ActivityTimelineRoutinesUseCase) : ViewModel() {
    val activityList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val activityList = activityTimelineRoutineUseCase.getActivityTimeline()
            emit(activityList)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}