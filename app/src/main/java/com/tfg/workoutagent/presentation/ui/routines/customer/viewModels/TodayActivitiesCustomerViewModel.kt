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
                val repetitions = mutableListOf<Int>()
                val weights = mutableListOf<Double>()
                if(routineActivity.repetitionsCustomer.isEmpty()){
                    routineActivity.repetitions.forEach { it -> repetitions.add(it.toInt())}
                    routineActivity.weightsPerRepetition.forEach { it -> weights.add(it.toDouble())}
                    routineActivity.repetitionsCustomer.clear()
                    routineActivity.weightsPerRepetitionCustomer.clear()
                    routineActivity.repetitionsCustomer.addAll(repetitions)
                    routineActivity.weightsPerRepetitionCustomer.addAll(weights)
                }else{
                    routineActivity.repetitionsCustomer.forEach { it -> repetitions.add(it.toInt())}
                    routineActivity.weightsPerRepetitionCustomer.forEach { it -> weights.add(it.toDouble())}
                    routineActivity.repetitionsCustomer.clear()
                    routineActivity.weightsPerRepetitionCustomer.clear()
                    routineActivity.repetitionsCustomer.addAll(repetitions)
                    routineActivity.weightsPerRepetitionCustomer.addAll(weights)
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
            this.newTodayActivities[activityPos].sets[activityPos] = newActivitySet
            this._executedDay.value?.activities?.get(activityPos)?.repetitionsCustomer?.set(setPosition, repetition)
            this._executedDay.value?.activities?.get(activityPos)?.weightsPerRepetitionCustomer?.set(setPosition, weight)
            updateDay(_executedDay.value!!)
            return ""
        }
        return "Check that the weight and repetitions entered are valid"
    }

    private fun checkSet(repetitions: Int?, weight: Double?): Boolean {
        return repetitions != null && repetitions>0 && weight != null && weight >= 0.0
    }

    fun removeSetActivity(activityPos: Int, positionSet: Int){
        /*
        val posActivity = getOldPosActivity(activity)
        this.oldActivities[posActivity].repetitions.removeAt(positionSet)
        this.oldActivities[posActivity].weightsPerRepetition.removeAt(positionSet)
        this.oldActivities[posActivity].sets--
        this.newTodayActivities[posActivity].sets.removeAt(positionSet)

         */
    }

    fun completedActivity(position:Int){
        val todayActivity = this.newTodayActivities[position]
        val todayRepetitions = mutableListOf<Int>()
        val todayWeights = mutableListOf<Double>()
        for (set in todayActivity.sets){
            todayRepetitions.add(set.repetitions)
            todayWeights.add(set.weight)
        }
        val activity = this._executedDay.value?.activities?.get(position)!!
        activity.repetitionsCustomer.addAll(todayRepetitions)
        activity.weightsPerRepetitionCustomer.addAll(todayWeights)
        activity.sets = activity.weightsPerRepetitionCustomer.size
        activity.completed = true
        this._executedDay.value?.activities?.set(position, activity)
        updateDay(_executedDay.value!!)
    }

    private fun updateDay(day: Day){
        viewModelScope.launch {
            try {
                manageRoutineCustomerUseCase.updateDay(
                    day
                )
                _dayLoaded.value = true
            } catch (e: Exception) {
                _dayLoaded.value = false
            }
            _dayLoaded.value = null
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
