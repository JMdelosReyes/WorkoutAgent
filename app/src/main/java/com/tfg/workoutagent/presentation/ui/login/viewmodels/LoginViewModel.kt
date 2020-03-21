package com.tfg.workoutagent.presentation.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.login.LoginUseCase
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
}