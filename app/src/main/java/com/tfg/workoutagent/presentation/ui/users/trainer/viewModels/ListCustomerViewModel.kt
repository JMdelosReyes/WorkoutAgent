package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.userUseCases.ListCustomerUseCase
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ListCustomerViewModel(listCustomerUseCase: ListCustomerUseCase) : ViewModel() {
    var filteredCustomerList = mutableListOf<Customer>()
    val customerList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customerList = listCustomerUseCase.getOwnCustomers()
            emit(customerList)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}