package com.tfg.workoutagent.presentation.ui.profile.admin.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.profile.admin.viewModels.EditProfileAdminViewModel

class EditProfileAdminFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileAdminFragment()
    }

    private lateinit var viewModel: EditProfileAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile_admin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
