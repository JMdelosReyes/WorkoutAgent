package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.ActivityListAdapter
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.launch
import java.util.*

class CreateRoutineViewModel(private val manageRoutineUseCase: ManageRoutineUseCase) :
    ViewModel() {

    //Routine fields
    val title = MutableLiveData<String>()
    val startDate = MutableLiveData<String>()
    val days = MutableLiveData<MutableList<Day>>(mutableListOf())

    //Day fields
    val dayName = MutableLiveData<String>()
    val activities = MutableLiveData<MutableList<RoutineActivity>>(mutableListOf())

    //Activity fields
    val sets = MutableLiveData<String>()
    val repetitions = MutableLiveData<String>()
    val weights = MutableLiveData<String>()
    val activityExercise = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    val selectedExercise = MutableLiveData<Exercise>()

    private val _routineCreated = MutableLiveData<Boolean?>(null)
    val routineCreated: LiveData<Boolean?>
        get() = _routineCreated

    private val _addDay = MutableLiveData<Boolean?>(null)
    val addDay: LiveData<Boolean?>
        get() = _addDay

    // Para el spinner
    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>>
        get() = _exercises

    // TODO
    var adapter: ActivityListAdapter? = null

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
                        startDate = Date(),
                        days = days.value!!

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

    fun onSaveDay() {
        val day = Day(name = dayName.value.toString(), activities = activities.value!!)
        days.value!!.add(day)
        clearDayData()
    }

    fun onSaveActivity() {
        val activity = RoutineActivity(
            name = activityExercise.value!!,
            sets = sets.value!!.toInt(),
            repetitions = repetitions.value!!.split(",").map { x ->
                x.toInt()
            } as MutableList<Int>,
            weightsPerRepetition = weights.value!!.split(",").map { x ->
                x.toDouble()
            } as MutableList<Double>,
            exercise = Exercise(),
            note = note.value!!
        )
        activities.value!!.add(activity)

        // TODO
        adapter?.notifyDataSetChanged()

        clearActivityData()
    }

    fun clearAllData() {
        title.value = ""
        startDate.value = ""
        dayName.value = ""
        days.value = mutableListOf()
    }

    fun clearDayData() {
        dayName.value = ""
        activities.value = mutableListOf()
    }

    fun clearActivityData() {
        sets.value = ""
        repetitions.value = ""
        weights.value = ""
        activityExercise.value = ""
        note.value = ""
    }

    // Para ek spinner
    fun getExercises() {
        viewModelScope.launch {
            try {
                val res = manageRoutineUseCase.getExercises()
                if (res is Resource.Success) {
                    _exercises.value = res.data
                }
            } catch (e: Exception) {
                _exercises.value = mutableListOf()
            }
        }
    }

    // Para el spinner
    fun selectExercise(exercise: Exercise) {
        selectedExercise.value = exercise
    }
}
