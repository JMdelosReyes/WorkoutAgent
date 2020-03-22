package com.tfg.workoutagent.presentation.ui.exercises.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.exercises.ListExercises
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ListExerciseViewModel(listExercise: ListExercises): ViewModel() {

    val exerciseList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            val exerciseList = listExercise.getExercises()
            emit(exerciseList)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}