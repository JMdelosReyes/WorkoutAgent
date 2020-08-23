package com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.workoutagent.data.repositoriesImpl.StorageRepositoryImpl
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCase
import com.tfg.workoutagent.domain.storageUseCases.ManageFilesUseCaseImpl
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateExerciseViewModel(private val manageExerciseUseCase: ManageExerciseUseCase) :
    ViewModel() {

    var title: String = ""
    private val _titleError = MutableLiveData("")
    val titleError: LiveData<String>
        get() = _titleError

    var description: String = ""
    private val _descriptionError = MutableLiveData("")
    val descriptionError: LiveData<String>
        get() = _descriptionError

    var tags: MutableList<String> = mutableListOf()
    private val _tagsError = MutableLiveData("")
    val tagsError: LiveData<String>
        get() = _tagsError
    var dataPhoto : Intent? = null
    val photosUris = mutableListOf<Uri>()
    private val _photosError =  MutableLiveData("")
    val photosError: LiveData<String>
        get() = _photosError

    // TODO Define error

    private val _exerciseCreated = MutableLiveData<Boolean?>(null)
    val exerciseCreated: LiveData<Boolean?>
        get() = _exerciseCreated

    private val _showLoading = MutableLiveData<Boolean?>(null)
    val showLoading: LiveData<Boolean?>
        get() = _showLoading

    fun loadingShowed() {
        _showLoading.value = null
    }

    fun onSubmit() {
        if (checkData()) {
            createExercise()
        }
    }

    fun addTag(string: String) = tags.add(string)
    fun removeTag(index: Int) = tags.removeAt(index)
    fun addPhoto(uri: Uri) = photosUris.add(uri)
    fun removePhoto(uri: Uri) = photosUris.removeAt(photosUris.indexOf(uri))
    fun clearPhotos() = photosUris.clear()

    private fun createExercise() {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                if(dataPhoto != null) {
                    val upload = ManageFilesUseCaseImpl(StorageRepositoryImpl())
                    when(val photoUris = upload.uploadMultipleImages(dataPhoto!!)) {
                        is Resource.Success -> {
                            manageExerciseUseCase.createExercise(
                                Exercise(
                                    title = title.trim(),
                                    description = description.trim(),
                                    tags = tags,
                                    photos = photoUris.data
                                )
                            )
                            _exerciseCreated.value = true
                            _showLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _showLoading.value = false
                _exerciseCreated.value = false
            }
            _exerciseCreated.value = null
        }
    }

    private fun checkData(): Boolean {
        checkTitle()
        checkDescription()
        checkTags()
        checkPhotos()
        return _titleError.value == "" && _descriptionError.value == "" && _tagsError.value == "" && _photosError.value == ""
    }

    private fun checkTitle() {
        title.let {
            if (it.length < 4 || it.length > 100) {
                _titleError.value = "The title must be between 4 and 100 characters"
                return
            }
            _titleError.value = ""
        }
    }

    private fun checkDescription() {
        description.let {
            if (it.length < 10 || it.length > 20000) {
                _descriptionError.value = "The description must be between 10 and 20000 characters"
                return
            }
            _descriptionError.value = ""
        }
    }

    private fun checkTags() {
        tags.let {
            if (it.isEmpty()) {
                _tagsError.value = "At least a tag is required"
                return
            }
            _tagsError.value = ""
        }
    }
    private fun checkPhotos(){
        if(dataPhoto == null){
            _photosError.value = "At least an image is required"
            return
        }else{
            if(photosUris.isEmpty()){
                _photosError.value = "At least an image is required"
                return
            }
            _photosError.value = ""
        }
    }

}