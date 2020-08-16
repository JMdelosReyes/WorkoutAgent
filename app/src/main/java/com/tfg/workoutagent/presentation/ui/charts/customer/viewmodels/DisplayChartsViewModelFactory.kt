package com.tfg.workoutagent.presentation.ui.charts.customer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCase

class DisplayChartsViewModelFactory(private val displayProfileUserUseCase: DisplayProfileUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DisplayProfileUserUseCase::class.java).newInstance(displayProfileUserUseCase)
    }
}