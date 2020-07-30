package com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.goalUseCases.ManageGoalUseCase
import com.tfg.workoutagent.models.Goal
import com.tfg.workoutagent.vo.utils.parseStringToDateBar
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CreateGoalCustomerViewModel(private val manageGoalUseCase: ManageGoalUseCase) : ViewModel() {

    var aim: String = ""
    private val _aimError = MutableLiveData("")
    val aimError: LiveData<String>
        get() = _aimError

    var description: String = ""
    private val _descriptionError = MutableLiveData("")
    val descriptionError: LiveData<String>
        get() = _descriptionError

    val pickerStartDate = MutableLiveData<Date>()
    val startDate = MutableLiveData("")
    private val _startDateError = MutableLiveData("")
    val startDateError: LiveData<String>
        get() = _startDateError

    val pickerEndDate = MutableLiveData<Date>()
    val endDate = MutableLiveData("")
    private val _endDateError = MutableLiveData("")
    val endDateError: LiveData<String>
        get() = _endDateError

    // TODO Define error
    fun goalCreated(){
        _goalCreated.value = null
    }

    private val _goalCreated = MutableLiveData<Boolean?>(null)
    val goalCreated: LiveData<Boolean?>
        get() = _goalCreated

    fun onSubmit() {
        if (checkData()) {
            createGoal()
        }
    }

    private fun createGoal() {
        viewModelScope.launch {
            try {
                manageGoalUseCase.createGoal(
                    Goal(
                        aim = aim,
                        description = description,
                        startDate = parseStringToDateBar(startDate.value!!)!!,
                        endDate = parseStringToDateBar(endDate.value!!)!!
                    )
                )
                _goalCreated.value = true
            }catch (e: Exception){
                _goalCreated.value = false
            }
        }
    }

    private fun checkData(): Boolean {
        checkAim()
        checkDescription()
        checkDates()
        return _aimError.value == "" && _descriptionError.value == "" && _startDateError.value == "" && _endDateError.value == ""
    }

    private fun checkAim(){
        aim.let {
            if (it.length < 4 || it.length > 30) {
                _aimError.value = "The aim must be between 4 and 30 characters"
                return
            }
            _aimError.value = ""
        }
    }

    private fun checkDescription() {
        description.let {
            if (it.length < 10 || it.length > 250) {
                _descriptionError.value = "The description must be between 10 and 250 characters"
                return
            }
            _descriptionError.value = ""
        }
    }

    private fun checkDates(){
        val currentTime = Calendar.getInstance().time
        startDate.let {
            if(!(it.value == null || it.value == "")){
                val date = parseStringToDateBar(it.value!!)!!
                if(date < currentTime){
                    _startDateError.value = "Start date should be after today."
                    return
                }
                else{
                    _startDateError.value = ""
                }
            }else{
                _startDateError.value = "Start date should not be null"
                return
            }
        }
        endDate.let {
            if(!(it.value == null || it.value == "")){
                val date = parseStringToDateBar(it.value!!)!!
                if(date < currentTime){
                    _endDateError.value = "Deadline should be after today."
                    return
                }else{
                    if(startDate.value != null){
                        val startDate = parseStringToDateBar(startDate.value!!)!!
                        if(startDate > date){
                            _endDateError.value = "Deadline should be after start date."
                            return
                        }else{
                            _endDateError.value = ""
                        }
                    }
                }
            }else{
                _endDateError.value = "Deadline should not be null"
                return
            }

        }
    }

    fun setStartDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerStartDate.value = Date(time)
        val date = simpleDateFormat.format(pickerStartDate.value)
        startDate.value = date
    }
    fun setEndDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerEndDate.value = Date(time)
        val date = simpleDateFormat.format(pickerEndDate.value)
        endDate.value = date
    }
}
