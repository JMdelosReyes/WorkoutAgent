package com.tfg.workoutagent.presentation.ui.profile.trainer.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.EditProfileTrainerViewModel

class EditProfileTrainerFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileTrainerFragment()
    }

    private lateinit var viewModel: EditProfileTrainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile_trainer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileTrainerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
