package com.tfg.workoutagent.presentation.ui.nutrition.trainer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCase

class EditNutritionCustomerTrainerViewModelFactory(private val customerId: String, private val manageCustomerTrainerUseCase: ManageCustomerTrainerUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditNutritionCustomerTrainerViewModel(customerId, manageCustomerTrainerUseCase) as T
    }
}