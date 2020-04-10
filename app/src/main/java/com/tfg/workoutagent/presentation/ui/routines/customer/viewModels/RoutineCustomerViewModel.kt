package com.tfg.workoutagent.presentation.ui.routines.customer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutineCustomerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Routine Fragment"
    }
    val text: LiveData<String> = _text
}
