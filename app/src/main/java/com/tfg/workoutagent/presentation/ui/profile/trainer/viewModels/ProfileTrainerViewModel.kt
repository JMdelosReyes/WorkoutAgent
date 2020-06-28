package com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ProfileTrainerViewModel(private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModel() {
    val getProfileTrainer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val trainer = displayProfileUserUseCase.getLoggedUserTrainer()
            emit(trainer)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}