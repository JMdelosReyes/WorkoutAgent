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
import java.lang.Double.parseDouble
import java.text.SimpleDateFormat
import java.util.*


class CreateRoutineViewModel(private val manageRoutineUseCase: ManageRoutineUseCase) :
    ViewModel() {

    // Routine fields
    val title = MutableLiveData<String>()
    val startDate = MutableLiveData<String>()
    val days = MutableLiveData<MutableList<Day>>(mutableListOf())
    val pickerDate = MutableLiveData<Date>()

    // Day fields
    val dayName = MutableLiveData<String>("")
    private val _dayNameError = MutableLiveData("")
    val dayNameError: LiveData<String>
        get() = _dayNameError

    val activities = MutableLiveData<MutableList<RoutineActivity>>(mutableListOf())
    private val _activitiesError = MutableLiveData("")
    val activitiesError: LiveData<String>
        get() = _activitiesError

    private val _dayCreated = MutableLiveData<Boolean?>(null)
    val dayCreated: LiveData<Boolean?>
        get() = _dayCreated

    // Activity fields
    val sets = MutableLiveData("")
    private val _setsError = MutableLiveData("")
    val setsError: LiveData<String>
        get() = _setsError

    val repetitions = MutableLiveData<String>("")
    private val _repetitionsError = MutableLiveData("")
    val repetitionsError: LiveData<String>
        get() = _repetitionsError

    val weights = MutableLiveData<String>("")
    private val _weightsError = MutableLiveData("")
    val weightsError: LiveData<String>
        get() = _weightsError

    val note = MutableLiveData<String>()
    private val _noteError = MutableLiveData("")
    val noteError: LiveData<String>
        get() = _noteError

    private val selectedExercise = MutableLiveData<Exercise>()

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

    // Para cerrar el dialog de activity
    private val _closeActivityDialog = MutableLiveData<Boolean>(null)
    val closeActivityDialog: LiveData<Boolean>
        get() = _closeActivityDialog

    fun onSubmit() {
        if (checkData()) {
            createRoutine()
        }
    }

    fun onAddDay() {
        _addDay.value = true
    }

    fun activityDialogClosed() {
        _closeActivityDialog.value = null
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
        if (checkDayData()) {
            saveDay()
        }
    }

    private fun checkDayData(): Boolean {
        checkDayName()
        checkDayActivities()
        return _dayNameError.value == "" && _activitiesError.value == ""
    }

    private fun checkDayActivities() {
        activities.value?.let {
            if (it.size == 0) {
                _activitiesError.value = "At least one activity is required"
                return
            }
        } ?: run {
            _activitiesError.value = "At least one activity is required"
            return
        }
        _activitiesError.value = ""
    }

    private fun checkDayName() {
        dayName.value?.let {
            if (it.isEmpty()) {
                _dayNameError.value = "The name of the day cannot be blank"
                return
            }
            if (it.length < 4 || it.length > 30) {
                _dayNameError.value = "The name must be between 4 and 30 characters"
                return
            }
        }
        _dayNameError.value = ""
    }

    private fun saveDay() {
        val day = Day(name = dayName.value.toString(), activities = activities.value!!)
        days.value!!.add(day)
        _dayCreated.value = true
        clearDayData()
        _dayCreated.value = null
    }

    fun onSaveActivity() {
        if (checkActivityData()) {
            saveActivity()
        }
    }

    private fun checkActivityData(): Boolean {
        checkActivitySets()
        checkActivityRepetitions()
        checkActivityWeights()
        checkActivityNote()
        return _setsError.value == "" && _repetitionsError.value == "" && _weightsError.value == "" && _noteError.value == ""
    }

    private fun checkActivityNote() {
        note.value?.let {
            if (it.length > 100) {
                _noteError.value = "Note too long"
                return
            }
        }
        _noteError.value = ""
    }

    private fun checkActivityWeights() {
        weights.value?.let {
            if (it.isEmpty()) {
                _weightsError.value = "Weights cannot be empty"
                return
            }
            val weightsList = it.split(",").map { x -> x.trim() }
            if (weightsList.size != sets.value?.toIntOrNull()) {
                _weightsError.value = "Weights must match the number of sets"
                return
            }
            weightsList.forEach { w ->
                if (w.toIntOrNull() == null) {
                    _weightsError.value = "All weights must be numeric"
                    return@let
                }
            }
            _weightsError.value = ""
        }
    }

    private fun checkActivityRepetitions() {
        repetitions.value?.let {
            if (it.isEmpty()) {
                _repetitionsError.value = "Repetitions cannot be empty"
                return
            }
            val repsList = it.split(",").map { x -> x.trim() }
            if (repsList.size != sets.value?.toIntOrNull()) {
                _repetitionsError.value = "Repetitions must match the number of sets"
                return
            }
            repsList.forEach { reps ->
                if (reps.toIntOrNull() == null) {
                    _repetitionsError.value = "All reps must be numeric"
                    return@let
                }
            }
            _repetitionsError.value = ""
        }
    }

    private fun checkActivitySets() {
        sets.value?.let {
            if (it.isEmpty()) {
                _setsError.value = "Sets cannot be empty"
                return
            } else {
                try {
                    parseDouble(it)
                } catch (e: NumberFormatException) {
                    _setsError.value = "Sets have to be numeric"
                    return
                }
            }
            _setsError.value = ""
        }
    }

    private fun saveActivity() {
        val activity = RoutineActivity(
            name = selectedExercise.value?.title!!,
            sets = sets.value!!.toInt(),
            repetitions = repetitions.value!!.split(",").map { x ->
                x.trim().toInt()
            } as MutableList<Int>,
            weightsPerRepetition = weights.value!!.split(",").map { x ->
                x.trim().toDouble()
            } as MutableList<Double>,
            exercise = selectedExercise.value!!,
            note = note.value!!
        )
        activities.value!!.add(activity)
        Log.i("AAAA", activity.toString())

        // TODO
        adapter?.notifyDataSetChanged()

        _closeActivityDialog.value = true
        clearActivityData()
        _activitiesError.value = ""
    }

    fun clearAllData() {
        title.value = ""
        startDate.value = ""
        dayName.value = ""
        days.value = mutableListOf()
        pickerDate.value = Date()
    }

    fun clearDayData() {
        dayName.value = ""
        _dayNameError.value = ""
        activities.value = mutableListOf()
        _activitiesError.value = ""
    }

    fun clearActivityData() {
        sets.value = ""
        _setsError.value = ""
        repetitions.value = ""
        _repetitionsError.value = ""
        weights.value = ""
        _weightsError.value = ""
        note.value = ""
        _noteError.value = ""
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

    fun setDate(time: Long) {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        pickerDate.value = Date(time)
        val date = simpleDateFormat.format(pickerDate.value)
        startDate.value = date
    }
}
