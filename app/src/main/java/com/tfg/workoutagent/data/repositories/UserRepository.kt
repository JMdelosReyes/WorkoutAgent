package com.tfg.workoutagent.data.repositories

import com.tfg.workoutagent.models.Administrator
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource

interface UserRepository {

    suspend fun updateProfileAdmin(admin : Administrator)  : Resource<Boolean>
    suspend fun deleteCustomerAdmin(id : String) : Resource<Boolean>
    suspend fun getCustomersAdmin() : Resource<MutableList<Customer>>

    suspend fun getCustomer(id: String) : Resource<Customer>
    suspend fun createCustomer(customer: Customer) : Resource<Boolean>
    suspend fun updateCustomer(customer: Customer) : Resource<Boolean>
    suspend fun deleteCustomer(id : String): Resource<Boolean>
    suspend fun canDeleteCustomer(id : String) : Resource<Boolean>
    suspend fun getLoggedUserCustomer() : Resource<Customer>
    suspend fun updateProfileCustomer(customer: Customer)  : Resource<Boolean>
    suspend fun deleteLoggedCustomer(): Resource<Boolean>
    suspend fun getTrainerByCustomerId(customerId: String) : Resource<Trainer>
    suspend fun getAdminEmail() : Resource<String>
    suspend fun getTrainer(id: String) : Resource<Trainer>
    suspend fun createTrainer(trainer : Trainer) : Resource<Boolean>
    suspend fun updateTrainer(trainer : Trainer) : Resource<Boolean>
    suspend fun deleteTrainer(id: String) : Resource<Boolean>
    suspend fun getTrainersAdmin() : Resource<MutableList<Trainer>>
    suspend fun getLoggedUserTrainer() : Resource<Trainer>
    suspend fun getOwnCustomers(): Resource<MutableList<Customer>>
    suspend fun updateProfileTrainer(trainer: Trainer) : Resource<Boolean>
    suspend fun deleteLoggedTrainer() : Resource<Boolean>
}