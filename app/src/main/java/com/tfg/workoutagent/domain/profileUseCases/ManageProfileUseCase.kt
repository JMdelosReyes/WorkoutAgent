package com.tfg.workoutagent.domain.profileUseCases

import com.tfg.workoutagent.models.Administrator
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface ManageProfileUseCase {
    suspend fun editProfileAdmin(admin: Administrator) : Resource<Boolean>
    suspend fun editProfileCustomer(customer: Customer) : Resource<Boolean>
    suspend fun editProfileTrainer(trainer: Trainer) : Resource<Boolean>
    suspend fun getLoggedUserTrainer() : Resource<Trainer>
    suspend fun getTrainerByCustomerId(customerId: String) : Resource<Trainer>
    //suspend fun getOwnUserAdmin() : Resource<Administrator>
    suspend fun getAdminEmail() : Resource<String>
    suspend fun getLoggedUserCustomer() : Resource<Customer>
    suspend fun deleteLoggedTrainer() : Resource<Boolean>
    suspend fun deleteLoggedCustomer() : Resource<Boolean>
}