package com.tfg.workoutagent.presentation.ui.users.trainer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        return inflater.inflate(R.layout.fragment_edit_delete_customer_trainer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                }
                is Resource.Failure -> {
                }
            }
        })

        viewModel.customerDeleted.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    findNavController().navigate(EditDeleteCustomerTrainerFragmentDirections.actionEditDeleteCustomerTrainerFragmentToNavigationUsersTrainer())
                }
                false -> {

                }
            }
        })

    }
}
