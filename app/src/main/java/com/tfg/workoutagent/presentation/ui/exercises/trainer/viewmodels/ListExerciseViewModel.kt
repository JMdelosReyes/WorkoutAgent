package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.exerciseUseCases.ListExercisesUseCase
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListExerciseViewModel(private val listExerciseUseCase: ListExercisesUseCase) : ViewModel() {

    val exerciseList: MutableLiveData<Resource<MutableList<Exercise>>> = MutableLiveData()

    var filteredExerciseList = mutableListOf<Exercise>()

    /*val exerciseList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val exerciseList = listExerciseUseCase.getExercises()
            emit(exerciseList)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }*/

    /*fun reloadExercises() {
        Log.i("Ejercicios", "Antes de antes")
        exerciseList.switchMap {
            liveData(Dispatchers.IO) {
                Log.i("Ejercicios", "Antes")
                emit(Resource.Loading())
                try {
                    val asd = listExerciseUseCase.getExercises()
                    Log.i("Ejercicios", asd.toString())
                    val exerciseList = asd
                    emit(exerciseList)
                } catch (e: Exception) {
                    emit(Resource.Failure(e))
                }
            }
        }
    }*/

    fun reloadExercises() = viewModelScope.launch(Dispatchers.IO) {
        try {
            exerciseList.postValue(Resource.Loading())
            val asd = listExerciseUseCase.getExercises()
            if (asd is Resource.Success) {
                exerciseList.postValue(asd)
            }
        } catch (e: Exception) {
            exerciseList.postValue(Resource.Failure(e))
        }
    }
}