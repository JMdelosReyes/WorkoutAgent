package com.tfg.workoutagent.domain.profileUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface ManageProfileUseCase {
    suspend fun editProfileAdmin() : Resource<Boolean>
    suspend fun editProfileCustomer() : Resource<Boolean>
    suspend fun editProfileTrainer() : Resource<Boolean>
    suspend fun getLoggedUserTrainer() : Resource<Trainer>
    //suspend fun getOwnUserAdmin() : Resource<Administrator>
    suspend fun getLoggedUserCustomer() : Resource<Customer>
}