package com.tfg.workoutagent.domain.routineUseCases

import com.tfg.workoutagent.data.repositories.RoutineRepository
import com.tfg.workoutagent.data.repositories.UserRepository
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource
import java.util.*

class AssignRoutinesUseCaseImpl(
    private val routineRepository: RoutineRepository,
    private val userRepository: UserRepository
) :
    AssignRoutinesUseCase {

    override suspend fun getTemplateRoutines(): Resource<MutableList<Routine>> =
        routineRepository.getTemplateRoutines()

    override suspend fun getCustomers() = userRepository.getOwnCustomers()

    override suspend fun assignRoutine(
        customer: Customer,
        routineId: String,
        startDate: Date
    ) = routineRepository.assignRoutine(customer, routineId, startDate)
}