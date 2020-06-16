package com.tfg.workoutagent.domain.routineUseCases


import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.TimelineActivity
import com.tfg.workoutagent.vo.Resource

class ActivityTimelineRoutinesUseCaseImpl(private val repo:RoutineRepository): ActivityTimelineRoutinesUseCase {

    override suspend fun getActivityTimeline(): Resource<MutableList<TimelineActivity>> = repo.getActivityTimeline()
}