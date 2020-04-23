package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.DisplayCustomerAdminViewModel

class DisplayCustomerAdminFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayCustomerAdminFragment()
    }

    private lateinit var viewModel: DisplayCustomerAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_customer_admin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DisplayCustomerAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
