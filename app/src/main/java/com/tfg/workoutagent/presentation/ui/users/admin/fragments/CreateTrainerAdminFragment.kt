package com.tfg.workoutagent.presentation.ui.users.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentCreateTrainerAdminBinding
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCase
import com.tfg.workoutagent.domain.userUseCases.ManageTrainerAdminUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.CreateTrainerAdminViewModel
import com.tfg.workoutagent.presentation.ui.users.admin.viewModels.CreateTrainerAdminViewModelFactory
import com.tfg.workoutagent.presentation.ui.users.trainer.fragments.CreateCustomerTrainerFragmentDirections

class CreateTrainerAdminFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, CreateTrainerAdminViewModelFactory(
                ManageTrainerAdminUseCaseImpl(
                    UserRepositoryImpl())))
            .get(CreateTrainerAdminViewModel::class.java) }

    private lateinit var binding : FragmentCreateTrainerAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_trainer_admin, container, false)

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData(){
        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.trainerSurnameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.trainerNameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            binding.trainerBirthdayInputEdit.error =
                if (it != "") it else null
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.trainerEmailInputEdit.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.trainerDniInputEdit.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.trainerPhoneInputEdit.error =
                if (it != "") it else null
        })

        viewModel.createdTrainer.observe(viewLifecycleOwner, Observer {
            when(it){
                //TODO: Esconder la progressBar
                true -> {
                    findNavController().navigate(CreateTrainerAdminFragmentDirections.actionCreateTrainerAdminFragmentToNavigationAdminUsers())
                }
                false -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
