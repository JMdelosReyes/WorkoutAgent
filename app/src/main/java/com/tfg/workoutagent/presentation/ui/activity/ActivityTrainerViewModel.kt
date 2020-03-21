package com.tfg.workoutagent.presentation.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityTrainerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is My Activity Fragment"
    }
    val text: LiveData<String> = _text
}