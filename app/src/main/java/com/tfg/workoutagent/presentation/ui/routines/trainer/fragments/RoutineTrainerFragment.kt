package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.RoutineTrainerViewModel
import kotlinx.android.synthetic.main.fragment_trainer_routine.*

class RoutineTrainerFragment : Fragment() {

    private lateinit var viewModel: RoutineTrainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this@RoutineTrainerFragment).get(RoutineTrainerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trainer_routine, container, false)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            text_routine_fragment.text = it
        })
        return root
    }

}
