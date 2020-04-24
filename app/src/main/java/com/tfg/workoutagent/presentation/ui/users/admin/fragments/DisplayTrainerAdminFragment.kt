package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.DisplayTrainerAdminViewModel

class DisplayTrainerAdminFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayTrainerAdminFragment()
    }

    private lateinit var viewModel: DisplayTrainerAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_trainer_admin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DisplayTrainerAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
