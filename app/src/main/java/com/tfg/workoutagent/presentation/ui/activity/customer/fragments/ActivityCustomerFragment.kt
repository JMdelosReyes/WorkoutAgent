package com.tfg.workoutagent.presentation.ui.activity.customer.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.activity.customer.viewModels.ActivityCustomerViewModel

class ActivityCustomerFragment : Fragment() {

    companion object {
        fun newInstance() = ActivityCustomerFragment()
    }

    private lateinit var viewModel: ActivityCustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_customer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActivityCustomerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
