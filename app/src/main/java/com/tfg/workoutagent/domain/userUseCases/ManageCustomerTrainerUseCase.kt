package com.tfg.workoutagent.domain.userUseCases

import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource

interface ManageCustomerTrainerUseCase {
    suspend fun createCustomer(customer : Customer): Resource<Boolean>
    suspend fun getCustomer(id:String): Resource<Customer>
    suspend fun getOwnCustomers() : Resource<MutableList<Customer>>
    suspend fun canDeleteCustomer(id : String) : Resource<Boolean>
    suspend fun deleteCustomer(id:String) : Resource<Boolean>
    suspend fun updateCustomer(customer: Customer) : Resource<Boolean>
}