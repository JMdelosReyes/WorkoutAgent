package com.tfg.workoutagent.presentation.ui.activity.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.ActivityTimelineRoutinesUseCase

class ActivityTimelineTrainerViewModelFactory(private val activityTimelineRoutineUseCase: ActivityTimelineRoutinesUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ActivityTimelineRoutinesUseCase::class.java).newInstance(activityTimelineRoutineUseCase)
    }
}