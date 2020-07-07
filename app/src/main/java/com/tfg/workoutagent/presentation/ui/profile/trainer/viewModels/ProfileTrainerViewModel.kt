package com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ProfileTrainerViewModel(private val darkMode: Boolean, private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModel() {
    private val _currentTheme = MutableLiveData<Boolean>()
    val currentTheme: LiveData<Boolean>
        get() = _currentTheme

    private val _changeTheme = MutableLiveData<Boolean>()
    val changeTheme: LiveData<Boolean>
        get() = _changeTheme

    init {
        _currentTheme.value = darkMode
    }

    val getProfileTrainer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val trainer = displayProfileUserUseCase.getLoggedUserTrainer()
            emit(trainer)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun onChangeTheme() {
        _changeTheme.value = true
    }

    fun themeChanged() {
        _changeTheme.value = null
        _currentTheme.value = !currentTheme.value!!
    }
}