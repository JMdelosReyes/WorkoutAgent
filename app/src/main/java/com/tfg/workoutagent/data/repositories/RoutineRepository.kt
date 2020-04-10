package com.tfg.workoutagent.data.repositories


import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.TimelineActivity
import com.tfg.workoutagent.vo.Resource

interface RoutineRepository {

    suspend fun getOwnRoutines(): Resource<MutableList<Routine>>

    suspend fun getActivityTimeline(): Resource<MutableList<TimelineActivity>>
}