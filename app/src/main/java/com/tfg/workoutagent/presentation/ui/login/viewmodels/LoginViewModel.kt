package com.tfg.workoutagent.presentation.ui.login.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.iid.FirebaseInstanceId
import com.tfg.workoutagent.domain.loginUseCases.LoginUseCase
import com.tfg.workoutagent.models.Trainer
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    val fetchRole : MutableLiveData<Resource<String>> = MutableLiveData()
    //private val fetchRole: MutableLiveData<String> = MutableLiveData("")

    fun reloadRole() = viewModelScope.launch(Dispatchers.IO) {
        try {
            fetchRole.postValue(Resource.Loading())
            val res = loginUseCase.getRole()
            if (res is Resource.Success) {
                fetchRole.postValue(res)
            }
        } catch (e: Exception) {
            fetchRole.postValue(Resource.Failure(e))
        }
    }

    fun setNullRole(){
        fetchRole.value = null
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