package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.userUseCases.ListCustomerUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class CustomerListViewModel (listCustomerUseCase: ListCustomerUseCase): ViewModel(){

    val customersList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            val customersList = listCustomerUseCase.getOwnCustomers()
            emit(customersList)
        }catch (e : Exception){
            emit(Resource.Failure(e))
        }
    }
}