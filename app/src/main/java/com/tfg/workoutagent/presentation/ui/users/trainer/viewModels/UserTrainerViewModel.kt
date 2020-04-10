package com.tfg.workoutagent.presentation.ui.users.trainer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserTrainerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Users Fragment"
    }
    val text: LiveData<String> = _text
}
