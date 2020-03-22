package com.tfg.workoutagent.presentation.ui.exercises.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.domain.exercises.ListExercises

class ListExerciselViewModelFactory(private val listExercise: ListExercises):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ListExercises::class.java).newInstance(listExercise)
    }

}