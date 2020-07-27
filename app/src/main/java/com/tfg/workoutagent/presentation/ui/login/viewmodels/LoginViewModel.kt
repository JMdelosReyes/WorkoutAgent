package com.tfg.workoutagent.presentation.ui.login.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.tfg.workoutagent.domain.loginUseCases.LoginUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {


    var token = MutableLiveData("")
    private val _tokenLoaded = MutableLiveData<Boolean?>(null)
    val tokenLoaded: LiveData<Boolean?>
        get() = _tokenLoaded

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
    fun tokenLoaded() {
        _tokenLoaded.value = null
    }

    fun updatetoken(token: String) {
        this.token.value = token
    }

    fun loadtoken() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val tokenVal = loginUseCase.updateToken()
            if (tokenVal is Resource.Success<*>) {
                token.postValue(tokenVal.data as String?)
                _tokenLoaded.value = true
            }
        } catch (e: Exception) {
            Log.i("Trainer", "Fail")
        }
    }

}