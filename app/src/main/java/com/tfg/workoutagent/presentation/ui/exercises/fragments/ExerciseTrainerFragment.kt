package com.tfg.workoutagent.presentation.ui.exercises.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.tfg.workoutagent.R
import kotlinx.android.synthetic.main.fragment_trainer_exercise.*

class ExerciseTrainerFragment : Fragment() {

    private lateinit var viewModel: ExerciseTrainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@ExerciseTrainerFragment).get(ExerciseTrainerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trainer_exercise, container, false)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            text_exercise_fragment.text = it
        })
        return inflater.inflate(R.layout.fragment_trainer_exercise, container, false)
    }

}
