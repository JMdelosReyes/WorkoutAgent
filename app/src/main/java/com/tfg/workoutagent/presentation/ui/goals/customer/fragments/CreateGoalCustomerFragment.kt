package com.tfg.workoutagent.presentation.ui.goals.customer.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.GoalRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentCreateGoalCustomerBinding
import com.tfg.workoutagent.domain.goalUseCases.ManageGoalUseCaseImpl
import com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels.CreateGoalCustomerViewModel
import com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels.CreateGoalCustomerViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_goal_customer.*
import java.util.*

class CreateGoalCustomerFragment : Fragment() {

    companion object {
        fun newInstance() = CreateGoalCustomerFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this,  CreateGoalCustomerViewModelFactory(
            ManageGoalUseCaseImpl(GoalRepositoryImpl())
        )
        ).get(CreateGoalCustomerViewModel::class.java)
    }
    private lateinit var binding: FragmentCreateGoalCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_goal_customer,
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
        setupUI()
    }

    private fun setupUI(){
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker().setSelection(Date().time)
        val picker: MaterialDatePicker<*> = builder.build()
        goal_startDate_input_edit.setOnClickListener {
            picker.show(activity!!.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setStartDate(it as Long)
            }
        }
        val builder2: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker().setSelection(Date().time)
        val picker2: MaterialDatePicker<*> = builder2.build()
        goal_endDate_input_edit.setOnClickListener {
            picker2.show(activity!!.supportFragmentManager, picker.toString())
            picker2.addOnPositiveButtonClickListener {
                viewModel.setEndDate(it as Long)
            }
        }
    }

    private fun observeData(){
        viewModel.aimError.observe(viewLifecycleOwner, Observer {
            binding.goalAimInputEdit.error =
                if (it != "") it else null
        })
        viewModel.descriptionError.observe(viewLifecycleOwner, Observer {
            binding.goalDescriptionInputEdit.error =
                if (it != "") it else null
        })
        viewModel.dateError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.dateErrorMessage.text = it
                    binding.dateErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.dateErrorMessage.text = ""
                    binding.dateErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.dateErrorMessage.text = ""
                binding.dateErrorMessage.visibility = View.GONE
            }
        })
        viewModel.goalCreated.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    true -> {
                        Toast.makeText(context, "Goal created successfully", Toast.LENGTH_SHORT).show()
                        this.viewModel.goalCreated()
                        findNavController().navigate(CreateGoalCustomerFragmentDirections.actionCreateGoalCustomerFragmentToListGoalCustomerFragment())
                    }
                    false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}
