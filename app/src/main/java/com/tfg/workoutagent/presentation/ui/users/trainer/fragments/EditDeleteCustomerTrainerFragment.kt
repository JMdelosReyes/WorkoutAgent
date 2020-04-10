package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditDeleteCustomerTrainerBinding
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.EditDeleteCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.EditDeleteCustomerTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_edit_delete_customer_trainer.*

class EditDeleteCustomerTrainerFragment : Fragment() {

    private val customerId by lazy { EditDeleteCustomerTrainerFragmentArgs.fromBundle(arguments!!).customerId}
    private val viewModel by lazy { ViewModelProvider(this, EditDeleteCustomerTrainerViewModelFactory(customerId, ManageCustomerTrainerUseCaseImpl(UserRepositoryImpl()))).get(EditDeleteCustomerTrainerViewModel::class.java)}
    private lateinit var binding: FragmentEditDeleteCustomerTrainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_delete_customer_trainer,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return  this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }

    private fun setupUI(){
        delete_customer_button.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle(getString(R.string.alert_title_delete_user))
            builder.setMessage(getString(R.string.alert_message_delete))

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                dialog.dismiss()
                viewModel.onDelete()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                }
                is Resource.Failure -> {
                    //TODO: hideProgress()
                }
            }
        })

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

        viewModel.customerDeleted.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(EditDeleteCustomerTrainerFragmentDirections.actionEditDeleteCustomerTrainerFragmentToNavigationUsersTrainer())
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.customerEdited.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(EditDeleteCustomerTrainerFragmentDirections.actionEditDeleteCustomerTrainerFragmentToDisplayCustomer(customerId, "HOLA"))
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })

    }
}
