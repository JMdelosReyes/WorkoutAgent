package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditRoutineViewModel(
    private val routineId: String,
    private val manageRoutineUseCase: ManageRoutineUseCase
) : ViewModel() {

    // Routine fields
    val title = MutableLiveData("")
    private val _titleError = MutableLiveData("")
    val titleError: LiveData<String>
        get() = _titleError

    val startDate = MutableLiveData("")
    private val _startDateError = MutableLiveData("")
    val startDateError: LiveData<String>
        get() = _startDateError

    val days = MutableLiveData<MutableList<Day>>(mutableListOf())
    private val _daysError = MutableLiveData("")
    val daysError: LiveData<String>
        get() = _daysError

    private val _routineDeleted = MutableLiveData<Boolean?>(null)
    val routineDeleted: LiveData<Boolean?>
        get() = _routineDeleted

    private val _routineSaved = MutableLiveData<Boolean?>(null)
    val routineSaved: LiveData<Boolean?>
        get() = _routineSaved

    val routine = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routineVal = manageRoutineUseCase.getRoutine(routineId)
            emit(routineVal)
            if (routineVal is Resource.Success) {
                loadData(routineVal)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    val pickerDate = MutableLiveData<Date>()

    private fun loadData(routine: Resource.Success<Routine>) {
        title.postValue(routine.data.title)
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(routine.data.startDate)
        startDate.postValue(date)
        days.postValue(routine.data.days)

    }

    fun setDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerDate.value = Date(time)
        val date = simpleDateFormat.format(pickerDate.value)
        startDate.value = date
    }

    fun onSubmit() {
        if (checkData()) {
            editRoutine()
        }
    }

    private fun checkData(): Boolean {

        return true
    }

    private fun editRoutine() {
        viewModelScope.launch {
            try {
                manageRoutineUseCase.editRoutine(
                    Routine(
                        id = routineId,
                        title = title.value.toString(),
                        days = days.value!!,
                        customer = null

                    )
                )
                _routineSaved.value = true
            } catch (e: Exception) {
                _routineSaved.value = false
            }
            _routineSaved.value = null
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            try {
                manageRoutineUseCase.deleteRoutine(routineId)
                _routineDeleted.value = true
            } catch (e: Exception) {
                _routineDeleted.value = false
            }
            _routineDeleted.value = null
        }
    }

    fun removeDay(day: Day) {
        this.days.value?.remove(day)
    }
}