package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

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
import com.tfg.workoutagent.databinding.FragmentCreateCustomerTrainerBinding
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.CreateCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.CreateCustomerTrainerViewModelFactory

class CreateCustomerTrainerFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, CreateCustomerTrainerViewModelFactory(
                ManageCustomerTrainerUseCaseImpl(UserRepositoryImpl())
            )
        ).get(CreateCustomerTrainerViewModel::class.java)
    }

    private lateinit var binding : FragmentCreateCustomerTrainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_customer_trainer,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData(){
        viewModel.birthdayError.observe(viewLifecycleOwner, Observer {
            binding.customerBirthdayInputEdit.error =
                if (it != "") it else null
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.customerEmailInputEdit.error =
                if (it != "") it else null
        })

        viewModel.heightError.observe(viewLifecycleOwner, Observer {
            binding.formHeightInputUser.error =
                if (it != "") it else null
        })

        viewModel.initialWeightError.observe(viewLifecycleOwner, Observer {
            binding.formWeightInputUser.error =
                if (it != "") it else null
        })

        viewModel.dniError.observe(viewLifecycleOwner, Observer {
            binding.customerDniInputEdit.error =
                if (it != "") it else null
        })

        viewModel.phoneError.observe(viewLifecycleOwner, Observer {
            binding.customerPhoneInputEdit.error =
                if (it != "") it else null
        })

        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.customerSurnameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.customerNameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.customerCreated.observe(viewLifecycleOwner, Observer {
            when(it){
                //TODO: Esconder la progressBar
                true -> {
                    findNavController().navigate(
                        CreateCustomerTrainerFragmentDirections.actionCreateCustomerTrainerFragmentToNavigationUsersTrainer2()
                    )
                }
                false -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
