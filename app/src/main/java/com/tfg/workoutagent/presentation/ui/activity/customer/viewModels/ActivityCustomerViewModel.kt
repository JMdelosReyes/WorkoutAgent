package com.tfg.workoutagent.presentation.ui.activity.customer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityCustomerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is My Activity Fragment"
    }
    val text: LiveData<String> = _text
}
