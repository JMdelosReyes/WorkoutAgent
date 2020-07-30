package com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCase
import com.tfg.workoutagent.models.*
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.ActivityListAdapter
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class EditRoutineViewModel(
    private var routineId: String,
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

    private val _routineLoaded = MutableLiveData<Boolean?>(null)
    val routineLoaded: LiveData<Boolean?>
        get() = _routineLoaded

    private val _routineSaved = MutableLiveData<Boolean?>(null)
    val routineSaved: LiveData<Boolean?>
        get() = _routineSaved

    private val _addDay = MutableLiveData<Boolean?>(null)
    val addDay: LiveData<Boolean?>
        get() = _addDay

    fun routineLoaded() {
        _routineLoaded.value = null
    }

    fun updateRoutineId(routineId: String) {
        this.routineId = routineId
    }

    fun loadRoutine() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val routineVal = manageRoutineUseCase.getRoutine(routineId)
            if (routineVal is Resource.Success) {
                loadData(routineVal)
                _routineLoaded.value = true
            }
        } catch (e: Exception) {
            Log.i("Routine", "Fail")
        }
    }

    /*val routine = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            Log.i("Routine ViewModel", routineId)
            val routineVal = manageRoutineUseCase.getRoutine(routineId)
            emit(routineVal)
            if (routineVal is Resource.Success) {
                loadData(routineVal)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }*/

    val pickerDate = MutableLiveData<Date>()

    // Day fields
    val dayName = MutableLiveData("")
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

    val repetitions = MutableLiveData("")
    private val _repetitionsError = MutableLiveData("")
    val repetitionsError: LiveData<String>
        get() = _repetitionsError

    val weights = MutableLiveData("")
    private val _weightsError = MutableLiveData("")
    val weightsError: LiveData<String>
        get() = _weightsError

    val note = MutableLiveData<String>()
    private val _noteError = MutableLiveData("")
    val noteError: LiveData<String>
        get() = _noteError

    val selectedExercise = MutableLiveData<Exercise>()

    // Error para el nuevo selector
    private val _exerciseError = MutableLiveData("")
    val exerciseError: LiveData<String>
        get() = _exerciseError

    private val _routineCreated = MutableLiveData<Boolean?>(null)
    val routineCreated: LiveData<Boolean?>
        get() = _routineCreated

    // Para el spinner
    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>>
        get() = _exercises

    // Edit day
    private val _editingDay = MutableLiveData<Day>(null)
    val editingDay: LiveData<Day>
        get() = _editingDay
    private var editingDayPosition: Int? = null

    // Edit activity
    private val _editingActivity = MutableLiveData<RoutineActivity>(null)
    val editingActivity: LiveData<RoutineActivity>
        get() = _editingActivity
    private var editingActivityPosition: Int? = null

    // TODO
    var adapter: ActivityListAdapter? = null

    private var originalActivities = mutableListOf<RoutineActivity>()

    // Para cerrar el dialog de activity
    private val _closeActivityDialog = MutableLiveData<Boolean>(null)
    val closeActivityDialog: LiveData<Boolean>
        get() = _closeActivityDialog

    private fun loadData(routine: Resource.Success<Routine>) {
        title.postValue(routine.data.title)
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(routine.data.startDate)
        startDate.postValue(date)
        days.postValue(routine.data.days)
    }

    // Para el spinner
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

    fun onSubmit() {
        if (checkData()) {
            editRoutine()
        }
    }

    private fun checkData(): Boolean {
        checkTitle()
        checkStartDate()
        checkDays()
        return _titleError.value == "" && _startDateError.value == "" && _daysError.value == ""
    }

    private fun checkDays() {
        days.value?.let {
            if (it.size == 0) {
                _daysError.value = "At least one day is required"
                return
            }
        } ?: run {
            _daysError.value = "At least one day is required"
            return
        }
        _daysError.value = ""
    }

    private fun checkStartDate() {
        startDate.value?.let {
            if (it.isEmpty()) {
                _startDateError.value = "The start date cannot be empty"
                return
            }
        }
        _startDateError.value = ""
    }

    private fun checkTitle() {
        title.value?.let {
            if (it.isEmpty()) {
                _titleError.value = "The title cannot be blank"
                return
            }
            if (it.length < 4 || it.length > 30) {
                _titleError.value = "The title must be between 4 and 30 characters"
                return
            }
        }
        _titleError.value = ""
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

    fun onDeleteDay(day: Day) {
        this.days.value?.remove(day)
    }

    fun onAddDay() {
        _addDay.value = true
    }

    fun addDayNavigationCompleted() {
        _addDay.value = null
    }

    fun removeActivity(activity: RoutineActivity) {
        this.activities.value?.remove(activity)
        this.adapter?.notifyDataSetChanged()
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
        _daysError.value = ""
    }

    fun clearAllData() {
        title.value = ""
        _titleError.value = ""
        startDate.value = ""
        _startDateError.value = ""
        dayName.value = ""
        days.value = mutableListOf()
        _daysError.value = ""
        pickerDate.value = Date()
        clearDayData()
        clearActivityData()
    }

    fun clearDayData() {
        dayName.value = ""
        _dayNameError.value = ""
        activities.value = mutableListOf()
        _activitiesError.value = ""
        clearActivityData()
    }

    fun clearActivityData() {
        this.newSets = mutableListOf()
        sets.value = ""
        _setsError.value = ""
        repetitions.value = ""
        _repetitionsError.value = ""
        weights.value = ""
        _weightsError.value = ""
        note.value = ""
        _noteError.value = ""
        resetSelectedCategories()
        _exerciseError.value = ""
        newSelectedExercise = null
    }

    fun clearNothing() {
        // do nothing
    }

    fun activityDialogClosed() {
        _closeActivityDialog.value = null
    }

    fun onSaveActivity() {
        if (checkActivityData()) {
            saveActivity()
        }
    }

    private fun checkActivityData(): Boolean {
        // checkActivitySets()
        // checkActivityRepetitions()
        // checkActivityWeights()
        checkNewActivitySets()
        checkNewExercise()
        checkActivityNote()
        return _setsError.value == "" && _noteError.value == "" && _exerciseError.value == ""
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

    private fun checkNewExercise() {
        if (this.newSelectedExercise == null) {
            _exerciseError.value = "You have to select an exercise"
            return
        }
        _exerciseError.value = ""
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
                    Double.parseDouble(it)
                } catch (e: NumberFormatException) {
                    _setsError.value = "Sets have to be numeric"
                    return
                }
            }
            _setsError.value = ""
        }
    }

    private fun checkNewActivitySets() {
        newSets.let {
            if (it.isEmpty() || it.size == 0) {
                _setsError.value = "Sets cannot be empty"
                return
            }

            if (!it.all { s -> s.isValid }) {
                _setsError.value = "There's an invalid set"
                return
            }
            _setsError.value = ""
        }
    }

    private fun saveActivity() {
        val newRepetitions = this.newSets.map { s -> s.repetitions } as MutableList<Int>
        val newWeights = this.newSets.map { s -> s.weight } as MutableList<kotlin.Double>
        val activity = RoutineActivity(
            name = newSelectedExercise!!.title,
            sets = this.newSets.size,
            repetitions = newRepetitions,
            weightsPerRepetition = newWeights,
            exercise = newSelectedExercise!!,
            note = (if (note.value != null) note.value else "")!!
        )
        activities.value!!.add(activity)

        // TODO
        adapter?.notifyDataSetChanged()

        _closeActivityDialog.value = true
        clearActivityData()
        _activitiesError.value = ""
    }

    fun onSaveEditActivity() {
        if (checkActivityData()) {
            replaceActivity()
        }
    }

    private fun replaceActivity() {
        val newRepetitions = this.newSets.map { s -> s.repetitions } as MutableList<Int>
        val newWeights = this.newSets.map { s -> s.weight } as MutableList<kotlin.Double>
        val activity = RoutineActivity(
            name = newSelectedExercise!!.title,
            sets = this.newSets.size,
            repetitions = newRepetitions,
            weightsPerRepetition = newWeights,
            exercise = newSelectedExercise!!,
            note = (if (note.value != null) note.value else "")!!
        )

        activities.value!![this.editingActivityPosition!!] = activity

        // TODO
        adapter?.notifyDataSetChanged()

        _closeActivityDialog.value = true
        clearActivityData()
        _activitiesError.value = ""
    }

    fun onEditActivity(routineActivity: RoutineActivity) {
        this.newSets =
            this.formatSets(routineActivity.repetitions, routineActivity.weightsPerRepetition)
        this.newSelectedExercise = routineActivity.exercise
        this._editingActivity.value = routineActivity
        this.editingActivityPosition = this.activities.value?.indexOf(routineActivity)
        this.sets.value = routineActivity.sets.toString()
        this.repetitions.value = routineActivity.repetitions.joinToString(",")
        this.weights.value = doubleListToString(routineActivity.weightsPerRepetition)
        this.note.value = routineActivity.note
        this.selectedExercise.value = routineActivity.exercise
    }

    private fun formatSets(
        repetitions: MutableList<Int>,
        weightsPerRepetition: MutableList<kotlin.Double>
    ): MutableList<ActivitySet> {
        val setsTemp = mutableListOf<ActivitySet>()
        repetitions.forEachIndexed() { index, rep ->
            setsTemp.add(ActivitySet(rep, weightsPerRepetition[index]))
        }
        return setsTemp
    }

    private fun doubleListToString(doubleList: MutableList<kotlin.Double>): String {
        var listString = ""
        for ((i, item) in doubleList.withIndex()) {
            if (i == doubleList.size - 1) {
                listString += item.roundToInt()
            } else {
                listString = listString + item.roundToInt() + ","
            }
        }

        return listString
    }

    fun onSaveEditDay() {
        if (checkDayData()) {
            replaceDay()
        }
    }

    private fun replaceDay() {
        val day = Day(name = dayName.value.toString(), activities = activities.value!!)
        days.value!![this.editingDayPosition!!] = day
        _dayCreated.value = true
        clearDayData()
        _dayCreated.value = null
        _daysError.value = ""
        this.originalActivities = mutableListOf()
    }

    fun onCancelEditDay() {
        activities.value = originalActivities.toMutableList()
        this.editingDay.value?.activities = activities.value!!
        originalActivities = mutableListOf()
    }

    fun onEditDay(day: Day) {
        this._editingDay.value = day
        this.editingDayPosition = this.days.value?.indexOf(day)
        this.dayName.value = day.name
        this.activities.value = day.activities
        this.originalActivities = this.activities.value!!.toMutableList()
    }

    private var newSets = mutableListOf<ActivitySet>()

    fun getNewSets(): MutableList<ActivitySet> {
        val res: MutableList<ActivitySet> = mutableListOf()
        res.addAll(this.newSets)
        return res
    }

    fun checkSet(repetitions: Int?, weight: kotlin.Double?): Boolean {
        return repetitions != null && weight != null
    }

    fun updateSet(activitySet: ActivitySet, position: Int) {
        this.newSets[position] = activitySet
        this.checkNewActivitySets()
    }

    fun updateSet(isValid: Boolean, position: Int) {
        this.newSets[position] =
            ActivitySet(this.newSets[position].repetitions, this.newSets[position].weight, isValid)
        this.checkNewActivitySets()
    }

    fun addSet(repetitions: Int?, weight: kotlin.Double?): String {
        if (checkSet(repetitions, weight)) {
            this.newSets.add(ActivitySet(repetitions!!, weight!!))
            return ""
        }
        return "Error"
    }

    fun removeSet(position: Int) {
        this.newSets.removeAt(position)
    }

    private var selectedCategories = mutableListOf<SelectedCategory>()

    fun getSelectedCategories() = this.selectedCategories

    fun addSelectedCategory(categoryName: String, categoryPosition: Int) {
        this.selectedCategories.add(SelectedCategory(categoryName, categoryPosition))
    }

    fun removeSelectedCategory(categoryName: String, categoryPosition: Int) {
        this.selectedCategories.remove(SelectedCategory(categoryName, categoryPosition))
    }

    fun resetSelectedCategories() {
        this.selectedCategories = mutableListOf()
    }

    fun updateAvailableExercises(): MutableList<Exercise> {
        return this.exercises.value?.filter { it.tags.containsAll(this.selectedCategories.map { c -> c.name }) } as MutableList<Exercise>
    }

    private var newSelectedExercise: Exercise? = null

    fun getNewSelectedExercise() = this.newSelectedExercise

    fun updateNewSelectedExercise(exercise: Exercise?) {
        if (exercise != null) {
            _exerciseError.value = ""
        }
        this.newSelectedExercise = exercise
    }

    data class SelectedCategory(val name: String, val position: Int)
}