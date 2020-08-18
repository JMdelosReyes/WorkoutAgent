package com.tfg.workoutagent.presentation.ui.charts.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class DisplayCustomerChartsViewModel(
    private val customerId: String,
    private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase
) : ViewModel() {
    val getProfileCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = manageCustomerTrainerUseCase.getCustomer(customerId)
            emit(customer)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}