package com.tfg.workoutagent.presentation.ui.profile.customer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileCustomerViewModel(private val displayProfileUserUseCase: DisplayProfileUserUseCase) :
    ViewModel() {
    val getProfileCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = displayProfileUserUseCase.getLoggedUserCustomer()
            emit(customer)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    private val _weightAdded = MutableLiveData<Boolean?>(null)
    val weightAdded: LiveData<Boolean?>
        get() = _weightAdded

    fun weightAdded() {
        _weightAdded.value = null
    }

    fun addWeight(weight: Double) {
        viewModelScope.launch {
            try {
                displayProfileUserUseCase.addWeight(weight)
                _weightAdded.value = true
            } catch (e: Exception) {
                _weightAdded.value = false
            }
        }
    }
}