package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface ListUserAdminUseCase {
    suspend fun getCustomersAdmin() : Resource<MutableList<Customer>>
    suspend fun getTrainersAdmin() : Resource<MutableList<Trainer>>
}