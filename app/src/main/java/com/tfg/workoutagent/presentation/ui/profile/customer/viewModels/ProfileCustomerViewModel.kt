package com.tfg.workoutagent.presentation.ui.profile.customer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileCustomerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile customer Fragment"
    }
    val text: LiveData<String> = _text
}