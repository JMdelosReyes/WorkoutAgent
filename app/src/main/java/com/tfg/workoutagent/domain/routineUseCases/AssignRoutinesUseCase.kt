package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource
import java.util.*

interface AssignRoutinesUseCase {
    suspend fun getTemplateRoutines(): Resource<MutableList<Routine>>
    suspend fun getCustomers(): Resource<MutableList<Customer>>
    suspend fun assignRoutine(
        customer: Customer,
        routineId: String,
        startDate: Date
    ): Resource<Boolean>
}