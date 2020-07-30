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

    private var oldActivities = mutableListOf<RoutineActivity>()
    fun getOldActivities() : MutableList<RoutineActivity>{
        val res : MutableList<RoutineActivity> = mutableListOf()
        res.addAll(this.oldActivities)
        return res
    }

    private var newTodayActivities = mutableListOf<TodayActivity>()

    private fun loadValues(routine: Resource.Success<Routine>) {
        oldActivities = routine.data.days[0].activities
        val activities =  routine.data.days[0].activities
        val todayActivities = mutableListOf<TodayActivity>()
        for (activity in activities){
            val sets = TodayActivity.parseSets(activity.repetitions, activity.weightsPerRepetition)
            todayActivities.add(TodayActivity(activity, sets))
        }
        newTodayActivities = todayActivities
    }

    private fun getOldPosActivity(activity: RoutineActivity) : Int = this.oldActivities.indexOf(activity)

    fun updateSetActivityToday(positionSet: Int, repetition: Int, weight: Double, activity: RoutineActivity) : String{
        val posActivity = getOldPosActivity(activity)
        if(checkSet(repetition, weight)){
            this.newTodayActivities[posActivity].sets[positionSet].weight = weight
            this.newTodayActivities[posActivity].sets[positionSet].repetitions = repetition
            return ""
        }
        return "Error"
    }

    private fun checkSet(repetitions: Int?, weight: Double?): Boolean {
        return repetitions != null && repetitions>0 && weight != null && weight >= 0.0
    }

    fun removeSetActivity(activity: RoutineActivity, positionSet: Int){
        val posActivity = getOldPosActivity(activity)
        this.oldActivities[posActivity].repetitions.removeAt(positionSet)
        this.oldActivities[posActivity].weightsPerRepetition.removeAt(positionSet)
        this.newTodayActivities[posActivity].sets.removeAt(positionSet)
    }

    fun completedActivity(activity: RoutineActivity, position:Int){
        val posActivity = getOldPosActivity(activity)
        val todayActivity = this.newTodayActivities[posActivity]
        val todayRepetitions = mutableListOf<Int>()
        val todayWeights = mutableListOf<Double>()
        for (set in todayActivity.sets){
            todayRepetitions.add(set.repetitions)
            todayWeights.add(set.weight)
        }
        this.oldActivities[posActivity].repetitions.clear()
        this.oldActivities[posActivity].weightsPerRepetition.clear()
        this.oldActivities[posActivity].repetitions.addAll(todayRepetitions)
        this.oldActivities[posActivity].weightsPerRepetition.addAll(todayWeights)
    }

    data class TodayActivity(var activity: RoutineActivity, var sets : MutableList<ActivitySet>){
        companion object {
            fun parseSets(repetitions: MutableList<Int>, weights: MutableList<Double>) : MutableList<ActivitySet> {
                return repetitions.mapIndexed { index, repetition -> ActivitySet(repetition, weights[index])} as MutableList<ActivitySet>
            }
        }
        constructor(activity: RoutineActivity, repetitions: MutableList<Int>, weights: MutableList<Double>) : this(activity, parseSets(repetitions, weights))
    }

    /*
        fun onCompleteActivity(position: Int, activity: RoutineActivity){
            this.newActivities[position] = activity
        }

        private var newSets = mutableListOf<ActivitySet>()
        fun getNewSets() : MutableList<ActivitySet>{
            val res : MutableList<ActivitySet> = mutableListOf()
            res.addAll(this.newSets)
            return res
        }


        fun removeSet(position: Int, activity: RoutineActivity) {
            this.newTodayActivities.
            this.newSets.removeAt(position)
        }

        fun updateSet(position: Int, activitySet: ActivitySet, activity: RoutineActivity) : String {
            if(checkSet(activitySet.repetitions, activitySet.weight)){
                this.newSets[position] = activitySet
                return ""
            }
            return "Error"
        }
    */

}
