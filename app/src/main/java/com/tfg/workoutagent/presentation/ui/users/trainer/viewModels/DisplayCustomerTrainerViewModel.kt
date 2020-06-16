package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.userUseCases.DisplayCustomerTrainerUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class DisplayCustomerTrainerViewModel(private val customerId: String, private val displayCustomerTrainerUseCase: DisplayCustomerTrainerUseCase): ViewModel() {
    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = displayCustomerTrainerUseCase.getCustomer(customerId)
            emit(customer)
        }catch (e : Exception){
            Log.i("EXCEPTION", "$e")
            emit(Resource.Failure(e))
        }
    }
}
