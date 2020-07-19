package com.tfg.workoutagent.presentation.ui.goals.trainer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerTrainerUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListGoalCustomerTrainerViewModel(private val customerId: String, private val listGoalCustomerTrainerViewModel: ListGoalCustomerTrainerUseCase) : ViewModel() {
    val goalsList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val goals = listGoalCustomerTrainerViewModel.getGoalsByCustomerId(customerId)
            emit(goals)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}
