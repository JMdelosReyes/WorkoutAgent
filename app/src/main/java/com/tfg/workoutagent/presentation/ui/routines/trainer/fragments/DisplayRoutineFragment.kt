package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.DisplayRoutineViewModel

class DisplayRoutineFragment : Fragment() {

    private val routineId by lazy { DisplayRoutineFragmentArgs.fromBundle(arguments!!).routineId }
    private val routineTitle by lazy { DisplayRoutineFragmentArgs.fromBundle(arguments!!).routineTitle }

    companion object {
        fun newInstance() = DisplayRoutineFragment()
    }

    private lateinit var viewModel: DisplayRoutineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.display_routine_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DisplayRoutineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
