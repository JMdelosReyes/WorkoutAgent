package com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListGoalCustomerViewModel(listGoalCustomerUseCase: ListGoalCustomerUseCase) : ViewModel() {
    val goalsList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val goals = listGoalCustomerUseCase.getGoalsCustomer()
            emit(goals)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}
