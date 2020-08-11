package com.tfg.workoutagent.presentation.ui.charts.customer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class DisplayChartsViewModel(private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModel() {
    val getProfileCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = displayProfileUserUseCase.getLoggedUserCustomer()
            emit(customer)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}