package com.tfg.workoutagent.presentation.ui.profile.admin.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers

class ProfileAdminViewModel(displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModel() {

    val getProfileAdmin = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val admin = displayProfileUserUseCase.getLoggedUserAdmin()
            emit(admin)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}