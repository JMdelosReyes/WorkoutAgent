package com.tfg.workoutagent.presentation.ui.routines.customer.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.RoutineCustomerViewModel

class RoutineCustomerFragment : Fragment() {

    companion object {
        fun newInstance() = RoutineCustomerFragment()
    }

    private lateinit var viewModel: RoutineCustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.routine_customer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RoutineCustomerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
