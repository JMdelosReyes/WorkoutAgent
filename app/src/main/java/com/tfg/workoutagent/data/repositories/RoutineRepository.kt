package com.tfg.workoutagent.data.repositories


import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.models.TimelineActivity
import com.tfg.workoutagent.vo.Resource

interface RoutineRepository {

    suspend fun getOwnRoutines(): Resource<MutableList<Routine>>

    suspend fun getTemplateRoutines(): Resource<MutableList<Routine>>

    suspend fun getRoutine(id: String): Resource<Routine>

    suspend fun getActivityTimeline(): Resource<MutableList<TimelineActivity>>

    suspend fun createRoutine(routine: Routine): Resource<Boolean>

    suspend fun editRoutine(routine: Routine): Resource<Boolean>

    suspend fun deleteRoutine(id: String): Resource<Boolean>

    suspend fun getAssignedRoutine(): Resource<Routine>

    suspend fun getTodayActivities(): Resource<MutableList<RoutineActivity>>
}