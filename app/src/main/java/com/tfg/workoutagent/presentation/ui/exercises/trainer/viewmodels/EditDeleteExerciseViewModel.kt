package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCase
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDeleteExerciseViewModel(
    private val exerciseId: String,
    private val manageExerciseUseCase: ManageExerciseUseCase
) :
    ViewModel() {

    val title = MutableLiveData("")
    private val _titleError = MutableLiveData("")
    val titleError: LiveData<String>
        get() = _titleError

    var description = MutableLiveData("")
    private val _descriptionError = MutableLiveData("")
    val descriptionError: LiveData<String>
        get() = _descriptionError

    var tags = MutableLiveData("")
    private val _tagsError = MutableLiveData("")
    val tagsError: LiveData<String>
        get() = _tagsError

    var photos = MutableLiveData("")
    // TODO Define error

    val getExercise = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val exercise = manageExerciseUseCase.getExercise(exerciseId)
            emit(exercise)
            if (exercise is Resource.Success) {
                loadData(exercise)
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    private val _exerciseDeleted = MutableLiveData<Boolean?>(null)
    val exerciseDeleted: LiveData<Boolean?>
        get() = _exerciseDeleted

    private val _exerciseSaved = MutableLiveData<Boolean?>(null)
    val exerciseSaved: LiveData<Boolean?>
        get() = _exerciseSaved

    private fun loadData(exercise: Resource.Success<Exercise>) {
        title.postValue(exercise.data.title)
        description.postValue((exercise.data.description))
        tags.postValue((exercise.data.tags.joinToString(",")))
        photos.postValue((exercise.data.photos.joinToString(",")))
    }

    fun onSave() {
        if (checkData()) {
            editExercise()
        }
    }

    private fun checkData(): Boolean {
        checkTitle()
        checkDescription()
        checkTags()
        return _titleError.value == "" && _descriptionError.value == "" && _tagsError.value == ""
    }

    private fun checkTitle() {
        title.value?.let {
            if (it.length < 4 || it.length > 30) {
                _titleError.value = "The title must be between 4 and 30 characters"
                return
            }
            _titleError.value = ""
        }
    }

    private fun checkDescription() {
        description.value?.let {
            if (it.length < 10 || it.length > 100) {
                _descriptionError.value = "The description must be between 10 and 100 characters"
                return
            }
            _descriptionError.value = ""
        }
    }

    private fun checkTags() {
        tags.value?.let {
            if (it.isEmpty()) {
                _tagsError.value = "At least a tag is required"
                return
            }
            _tagsError.value = ""
        }
    }

    private fun editExercise() {
        viewModelScope.launch {
            try {
                manageExerciseUseCase.editExercise(
                    Exercise(
                        id = exerciseId,
                        title = title.value!!,
                        description = description.value!!,
                        tags = tags.value!!.split(",") as MutableList<String>,
                        photos = photos.value!!.split(",") as MutableList<String>
                    )
                )
                _exerciseSaved.value = true
            } catch (e: Exception) {
                _exerciseSaved.value = false
            }
            _exerciseSaved.value = null
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            try {
                manageExerciseUseCase.deleteExercise(exerciseId)
                _exerciseDeleted.value = true
            } catch (e: Exception) {
                _exerciseDeleted.value = false
            }
            _exerciseDeleted.value = null
        }
    }
}