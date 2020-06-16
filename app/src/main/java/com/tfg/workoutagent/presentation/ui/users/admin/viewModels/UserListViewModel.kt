package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.userUseCases.ListUserAdminUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class UserListViewModel(listUserAdminUseCase: ListUserAdminUseCase): ViewModel() {
    val trainerList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val users = listUserAdminUseCase.getTrainersAdmin()
            emit(users)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    val customerList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val users = listUserAdminUseCase.getCustomersAdmin()
            emit(users)
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }


}