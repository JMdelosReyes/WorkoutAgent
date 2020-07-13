package com.tfg.workoutagent.presentation.ui.profile.customer.viewModels

import androidx.lifecycle.*
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DeleteProfileSendEmailCustomerViewModel(private val id: String, private val manageProfileUseCase: ManageProfileUseCase) : ViewModel() {
    private val _userDeleted = MutableLiveData<Boolean?>(null)
    val userDeleted: LiveData<Boolean?>
        get() = _userDeleted

    val getCustomer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val customer = manageProfileUseCase.getLoggedUserCustomer()
            emit(customer)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    val getAdminEmail = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val emailAdmin = manageProfileUseCase.getAdminEmail()
            emit(emailAdmin)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    val getTrainerEmail = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val trainer = manageProfileUseCase.getTrainerByCustomerId(id)
            emit(trainer)
        }catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun onDelete(){
        viewModelScope.launch {
            try {
                manageProfileUseCase.deleteLoggedCustomer()
                _userDeleted.value = true
            }catch (e: Exception){
                _userDeleted.value = false
            }
        }
    }
}
