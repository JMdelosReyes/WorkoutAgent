package com.tfg.workoutagent.presentation.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.iid.FirebaseInstanceId
import com.tfg.workoutagent.domain.loginUseCases.LoginUseCase
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class LoginViewModel(loginUseCase: LoginUseCase) : ViewModel() {
    val fechtRole = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            val role = loginUseCase.getRole()
            emit(role)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
    val fetchToken = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
            try{
                val token = loginUseCase.updateToken()
                emit(token)
            }catch (e: Exception){
                emit(Resource.Failure(e))
        }
    }
    val trainer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            val t = loginUseCase.getTrainerByCustomerId()
            if (t is Resource.Success) {
                emit(t)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
    val loggedTrainer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            val t = loginUseCase.getLoggedUserTrainer()
            if (t is Resource.Success) {
                emit(t)
            }
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}