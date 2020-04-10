package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.models.Routine
import kotlinx.coroutines.launch
import java.util.*

class CreateRoutineViewModel(private val manageRoutineUseCase: ManageRoutineUseCase) :
    ViewModel() {

    val title = MutableLiveData<String>()
    val dayName = MutableLiveData<String>()
    val startDate = MutableLiveData<String>()
    val days = MutableLiveData<MutableList<Day>>()

    private val _routineCreated = MutableLiveData<Boolean?>(null)
    val routineCreated: LiveData<Boolean?>
        get() = _routineCreated

    private val _backToCreate = MutableLiveData<Boolean?>(null)
    val backToCreate: LiveData<Boolean?>
        get() = _backToCreate

    private val _addDay = MutableLiveData<Boolean?>(null)
    val addDay: LiveData<Boolean?>
        get() = _addDay



    fun onSubmit() {
        if (checkData()) {
            createRoutine()
        }
    }

    fun onAddDay() {
        _addDay.value = true
    }



    private fun createRoutine() {
        viewModelScope.launch {
            try {
                manageRoutineUseCase.createRoutine(
                    Routine(
                        title = title.value.toString(),
                        startDate = Date()
                    )
                )
                _routineCreated.value = true
            } catch (e: Exception) {
                _routineCreated.value = false
            }
            _routineCreated.value = null
        }
    }

    private fun checkData(): Boolean {
        return true
    }

    fun addDayNavigationCompleted() {
        _addDay.value = null
    }

    fun onSaveDay(){
        val day = Day(name = dayName.value.toString())
        days.value!!.add(day)
        clearDayData()
        Log.i("Days", "${days.value}")
    }

    fun clearAllData() {
        title.value = ""
        startDate.value = ""
        dayName.value = ""
        days.value = mutableListOf()
    }

    fun clearDayData() {
        dayName.value = ""
    }

    fun clearActivityData() {

    }

    fun onBackToCreate() {
        _backToCreate.value = true
    }

    fun backToCreateComplete() {
        _backToCreate.value = null
    }
}
