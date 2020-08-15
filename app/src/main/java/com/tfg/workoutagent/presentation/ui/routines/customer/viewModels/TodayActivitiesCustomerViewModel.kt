package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCase
import com.tfg.workoutagent.models.ActivitySet
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayActivitiesCustomerViewModel(private val manageRoutineCustomerUseCase: ManageRoutineCustomerUseCase) : ViewModel() {

    //Valores estimados

    private val _dayLoaded = MutableLiveData<Boolean?>(null)
    val dayLoaded: LiveData<Boolean?>
        get() = _dayLoaded

    fun dayLoaded() {
        _dayLoaded.value = null
    }

    private val _finishedDay = MutableLiveData<Boolean?> (null)
    val finishedDay: LiveData<Boolean?>
        get() = _finishedDay

    //Valores ejecutados
    private val _executedDay = MutableLiveData<Day>(null)
    val executedDay: LiveData<Day>
        get() = _executedDay

    private val _updatedActivity = MutableLiveData<Boolean?>(null)
    val updatedActivity: LiveData<Boolean?>
        get() = _updatedActivity


    val routine = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val routine = manageRoutineCustomerUseCase.getAssignedRoutine()
            if(routine is Resource.Success){
                loadValues(routine)
            }
            emit(routine)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    private var newTodayActivities = mutableListOf<TodayActivity>()

    private fun loadValues(routine: Resource.Success<Routine>) {
        var activities = mutableListOf<RoutineActivity>()
        for (day in routine.data.days){
            if(day.completed) continue
            activities = day.activities
            day.activities.forEach { routineActivity ->
                if(routineActivity.repetitionsCustomer.isEmpty()){
                    if(!routineActivity.completed){
                        routineActivity.repetitions.forEach { it ->  routineActivity.repetitionsCustomer.add(it.toInt())}
                        routineActivity.weightsPerRepetition.forEach { it -> routineActivity.weightsPerRepetitionCustomer.add(it.toDouble())}
                    }
                }else{
                    val repetsCus = mutableListOf<Int>()
                    val weightsCus = mutableListOf<Double>()
                    routineActivity.repetitionsCustomer.forEach { it ->  repetsCus.add(it.toInt())}
                    routineActivity.repetitionsCustomer.clear()
                    routineActivity.repetitionsCustomer.addAll(repetsCus)
                    routineActivity.weightsPerRepetitionCustomer.forEach { it -> weightsCus.add(it.toDouble())}
                    routineActivity.weightsPerRepetitionCustomer.clear()
                    routineActivity.weightsPerRepetitionCustomer.addAll(weightsCus)
                }
            }
            _executedDay.postValue(day)
            break
        }
        val todayActivities = mutableListOf<TodayActivity>()
        for (activity in activities){
            val sets = TodayActivity.parseSets(activity.repetitions, activity.weightsPerRepetition)
            todayActivities.add(TodayActivity(activity, sets))
        }
        newTodayActivities = todayActivities
    }

    fun updateSetActivityToday(activityPos: Int, setPosition: Int, repetition:Int?, weight: Double?) : String {
        if(checkSet(repetition, weight)){
            val newActivitySet = this.newTodayActivities[activityPos].sets[setPosition]
            newActivitySet.weight = weight!!
            newActivitySet.repetitions = repetition!!
            this.newTodayActivities[activityPos].sets[setPosition] = newActivitySet
            this._executedDay.value?.activities?.get(activityPos)?.repetitionsCustomer?.removeAt(setPosition)
            this._executedDay.value?.activities?.get(activityPos)?.repetitionsCustomer?.add(setPosition, repetition)
            this._executedDay.value?.activities?.get(activityPos)?.weightsPerRepetitionCustomer?.removeAt(setPosition)
            this._executedDay.value?.activities?.get(activityPos)?.weightsPerRepetitionCustomer?.add(setPosition, weight)
            updateDay(_executedDay.value!!)
            return ""
        }
        return "Check that the weight and repetitions entered are valid"
    }

    private fun checkSet(repetitions: Int?, weight: Double?): Boolean {
        return repetitions != null && repetitions>0 && weight != null && weight >= 0.0
    }

    fun removeSetActivity(activityPos: Int, positionSet: Int){
        this.newTodayActivities[activityPos].sets.removeAt(positionSet)
        this._executedDay.value?.activities?.get(activityPos)?.repetitionsCustomer?.removeAt(positionSet)
        this._executedDay.value?.activities?.get(activityPos)?.weightsPerRepetitionCustomer?.removeAt(positionSet)
        updateDay(_executedDay.value!!)
    }

    fun completedActivity(position:Int){
        this._executedDay.value?.activities?.get(position)?.completed = true
        updateDay(_executedDay.value!!)
    }
    fun finishDay(){
        this._executedDay.value?.activities?.forEach {
            it.completed = true
            if(it.repetitionsCustomer.isEmpty()){
                it.repetitionsCustomer.addAll(it.repetitions)
                it.weightsPerRepetitionCustomer.addAll(it.weightsPerRepetition)
            }
        }
        this._executedDay.value?.completed = true
        finishDay(_executedDay.value!!)
    }


    private fun updateDay(day: Day){
        viewModelScope.launch {
            try {
                manageRoutineCustomerUseCase.updateDay(
                    day
                )
                _dayLoaded.value = true
            } catch (e: Exception) {
                Log.i("updateDay", e.toString())
                _dayLoaded.value = false
            }
            _dayLoaded.value = null
        }
    }
    private fun finishDay(day: Day){
        viewModelScope.launch {
            try {
                manageRoutineCustomerUseCase.updateDay(
                    day
                )
                _finishedDay.value = true
            } catch (e: Exception) {
                Log.i("finishDay", e.toString())
                _finishedDay.value = false
            }
            _finishedDay.value = null
        }
    }

    data class TodayActivity(var activity: RoutineActivity, var sets : MutableList<ActivitySet>){
        companion object {
            fun parseSets(repetitions: MutableList<Int>, weights: MutableList<Double>) : MutableList<ActivitySet> {
                return repetitions.mapIndexed { index, repetition -> ActivitySet(repetition, weights[index])} as MutableList<ActivitySet>
            }
        }
        constructor(activity: RoutineActivity, repetitions: MutableList<Int>, weights: MutableList<Double>) : this(activity, parseSets(repetitions, weights))
    }

}
