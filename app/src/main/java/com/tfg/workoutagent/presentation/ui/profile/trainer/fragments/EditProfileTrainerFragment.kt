package com.tfg.workoutagent.presentation.ui.profile.trainer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.ManageProfileUseCaseImpl
import com.tfg.workoutagent.databinding.FragmentEditProfileTrainerBinding
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.EditProfileTrainerViewModel
import com.tfg.workoutagent.presentation.ui.profile.trainer.viewModels.EditProfileTrainerViewModelFactory

class EditProfileTrainerFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileTrainerFragment()
    }

    private val viewModel by lazy { ViewModelProvider(this, EditProfileTrainerViewModelFactory(ManageProfileUseCaseImpl(UserRepositoryImpl()))).get(EditProfileTrainerViewModel::class.java) }
    private lateinit var binding : FragmentEditProfileTrainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_profile_trainer,
            container,
            false)
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return  this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
        observeErrors()
    }

    private fun setupUI(){

    }
    private fun observeData(){

    }
    private fun observeErrors(){

    }
}
