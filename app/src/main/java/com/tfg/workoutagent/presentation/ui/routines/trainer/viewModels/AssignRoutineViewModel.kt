package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.routineUseCases.AssignRoutinesUseCase
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AssignRoutineViewModel(private val assignRoutinesUseCase: AssignRoutinesUseCase) :
    ViewModel() {

    val customers: MutableLiveData<Resource<MutableList<Customer>>> = MutableLiveData()

    val routines: MutableLiveData<Resource<MutableList<Routine>>> = MutableLiveData()

    /*fun loadCustomers() = viewModelScope.launch(Dispatchers.IO) {
        try {
            customers.value = Resource.Loading()
            val customerList = assignRoutineUseCase.getCustomers()
            if (customerList is Resource.Success) {
                customers.value = customerList
            }
        } catch (e: Exception) {
            customers.value = Resource.Failure(e)
        }
    }*/

    fun loadRoutines() = viewModelScope.launch(Dispatchers.IO) {
        try {
            routines.postValue(Resource.Loading())
            val routineList = assignRoutinesUseCase.getTemplateRoutines()
            if (routineList is Resource.Success) {
                routines.postValue(routineList)
            }
        } catch (e: Exception) {
            routines.postValue(Resource.Failure(e))
        }
    }
}