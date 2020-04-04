package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.TimelineActivity
import com.tfg.workoutagent.vo.Resource

interface ActivityTimelineRoutinesUseCase {

    suspend fun getActivityTimeline(): Resource<MutableList<TimelineActivity>>
}