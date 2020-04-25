package com.tfg.workoutagent.presentation.ui.users.admin.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCase
import com.tfg.workoutagent.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DisplayTrainerAdminViewModel(private val id: String, private val manageTrainerAdminUseCase: ManageTrainerAdminUseCase) : ViewModel() {
    val getTrainer = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val trainer = manageTrainerAdminUseCase.getTrainer(id)
            emit(trainer)
        }catch (e : Exception){
            Log.i("ExDisplayTrainerAdminVM", "${e}")
            emit(Resource.Failure(e))
        }
    }
}
