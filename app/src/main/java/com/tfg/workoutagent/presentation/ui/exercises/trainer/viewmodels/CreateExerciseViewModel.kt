package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCase
import com.tfg.workoutagent.models.Exercise
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateExerciseViewModel(private val manageExerciseUseCase: ManageExerciseUseCase) :
    ViewModel() {

    var title: String = ""
    var description: String = ""
    var tags: String = ""
    var photos: String = ""

    private val _exerciseCreated = MutableLiveData<Boolean?>(null)
    val exerciseCreated: LiveData<Boolean?>
        get() = _exerciseCreated

    fun onSubmit() {
        if (checkData()) {
            createExercise()
        }
    }

    private fun createExercise() {
        viewModelScope.launch {
            try {
                manageExerciseUseCase.createExercise(
                    Exercise(
                        title = title,
                        description = description,
                        tags = tags.split(",") as MutableList<String>,
                        photos = photos.split(",") as MutableList<String>
                    )
                )
                _exerciseCreated.value = true
            } catch (e: Exception) {
                _exerciseCreated.value = false
            }
            _exerciseCreated.value = null
        }
    }

    private fun checkData(): Boolean {
        return true
    }
}