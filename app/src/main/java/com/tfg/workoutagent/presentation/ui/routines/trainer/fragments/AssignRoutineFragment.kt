package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentAssignRoutineBinding
import com.tfg.workoutagent.domain.routineUseCases.AssignRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.CustomerListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.RoutineAssignListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.AssignRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.AssignRoutineViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_assign_routine.*
import java.util.*

class AssignRoutineFragment : DialogFragment() {

    private lateinit var binding: FragmentAssignRoutineBinding

    private lateinit var toolbar: Toolbar

    private val viewModel by lazy {
        ViewModelProvider(
            this, AssignRoutineViewModelFactory(
                AssignRoutinesUseCaseImpl(RoutineRepositoryImpl(), UserRepositoryImpl())
            )
        ).get(AssignRoutineViewModel::class.java)
    }

    private lateinit var customerListAdapter: CustomerListAdapter

    private lateinit var routineAssignListAdapter: RoutineAssignListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_assign_routine,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this
        this.toolbar = binding.root.findViewById(R.id.assign_routine_toolbar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        if (darkMode) {
            // rl_assign_routine.setBackgroundResource(R.drawable.item_no_border_dark)
            assign_routine_startDate_input.setBackgroundColor(context!!.resources.getColor(R.color.black_darkMode))
            assign_routine_startDate_input_edit.setBackgroundColor(context!!.resources.getColor(R.color.black_darkMode))
            assign_routine_startDate_input_edit.setTextColor(Color.WHITE)
        }

        setupToolbar()
    }

    override fun onStart() {
        super.onStart()
        setupDialogWindow()
        setupCustomerListRecycler()
        setupRoutineAssignListRecycler()
        setupCustomerSearchView()
        setupRoutineSearchView()
        setupPicker()
        observeData()
        observeErrors()
        observeDialogState()
    }

    private fun setupDialogWindow() {
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    private fun setupToolbar() {
        this.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        this.toolbar.title = "Assign routine"
        this.toolbar.inflateMenu(R.menu.assign_routine_dialog_menu)
        this.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_assign_routine -> {
                    this.viewModel.onAssignRoutine()
                }
                else -> {
                    dismiss()
                }
            }
            true
        }
    }

    private fun observeData() {
        this.viewModel.routines.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        this.routineAssignListAdapter.setListData(it.data)
                    }
                    is Resource.Failure -> {
                    }
                }
            }
        })

        this.viewModel.customers.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        this.customerListAdapter.setListData(it.data)
                    }
                    is Resource.Failure -> {
                    }
                }
            }
        })

        this.viewModel.loadRoutines()
        this.viewModel.loadCustomers()
    }

    private fun setupCustomerListRecycler() {
        this.customerListAdapter =
            CustomerListAdapter(requireContext()) clickListener@{ customer, _ ->
                if (this.viewModel.getSelectedCustomer() == customer) {
                    this.viewModel.updateSelectedCustomer(null)
                    this.customerListAdapter.setSelectedCustomer(null)
                } else {
                    this.viewModel.updateSelectedCustomer(customer)
                    this.customerListAdapter.setSelectedCustomer(customer)
                }
            }
        recyclerView_customers.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView_customers.adapter = this.customerListAdapter
    }

    private fun setupRoutineAssignListRecycler() {
        this.routineAssignListAdapter =
            RoutineAssignListAdapter(requireContext()) clickListener@{ routine, view ->
                if (this.viewModel.getSelectedRoutine() == routine) {
                    this.viewModel.updateSelectedRoutine(null)
                    this.routineAssignListAdapter.setSelectedRoutine(null)
                } else {
                    this.viewModel.updateSelectedRoutine(routine)
                    this.routineAssignListAdapter.setSelectedRoutine(routine)
                }
            }
        recyclerView_routines.layoutManager =
            LinearLayoutManager(this.requireContext())
        recyclerView_routines.adapter = this.routineAssignListAdapter
    }

    private fun setupCustomerSearchView() {
        search_view_customer.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val lowerText = query?.toLowerCase(Locale.ROOT)
                viewModel.filterCustomers(lowerText)
                if (!viewModel.getFilteredCustomers().contains(viewModel.getSelectedCustomer())) {
                    viewModel.updateSelectedCustomer(null)
                }
                customerListAdapter.setListData(viewModel.getFilteredCustomers())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val lowerText = newText?.toLowerCase(Locale.ROOT)
                viewModel.filterCustomers(lowerText)
                if (!viewModel.getFilteredCustomers().contains(viewModel.getSelectedCustomer())) {
                    viewModel.updateSelectedCustomer(null)
                }
                customerListAdapter.setListData(viewModel.getFilteredCustomers())
                return true
            }
        })
    }

    private fun setupRoutineSearchView() {
        search_view_routine.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val lowerText = query?.toLowerCase(Locale.ROOT)
                viewModel.filterRoutines(lowerText)
                if (!viewModel.getFilteredRoutines().contains(viewModel.getSelectedRoutine())) {
                    viewModel.updateSelectedRoutine(null)
                }
                routineAssignListAdapter.setListData(viewModel.getFilteredRoutines())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val lowerText = newText?.toLowerCase(Locale.ROOT)
                viewModel.filterRoutines(lowerText)
                if (!viewModel.getFilteredRoutines().contains(viewModel.getSelectedRoutine())) {
                    viewModel.updateSelectedRoutine(null)
                }
                routineAssignListAdapter.setListData(viewModel.getFilteredRoutines())
                return true
            }
        })
    }

    private fun setupPicker() {
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.pickerDate.value!!.time)
        val picker: MaterialDatePicker<*> = builder.build()
        assign_routine_startDate_input_edit.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
        }
    }

    private fun observeErrors() {
        viewModel.customerError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.assignRoutineCustomerErrorMessage.text = it
                    binding.assignRoutineCustomerErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.assignRoutineCustomerErrorMessage.text = ""
                    binding.assignRoutineCustomerErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.assignRoutineCustomerErrorMessage.text = ""
                binding.assignRoutineCustomerErrorMessage.visibility = View.GONE
            }
        })

        viewModel.routineError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.assignRoutineRoutineErrorMessage.text = it
                    binding.assignRoutineRoutineErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.assignRoutineRoutineErrorMessage.text = ""
                    binding.assignRoutineRoutineErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.assignRoutineRoutineErrorMessage.text = ""
                binding.assignRoutineRoutineErrorMessage.visibility = View.GONE
            }
        })

        viewModel.startDateError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it != "") {
                    binding.assignRoutineStartDateErrorMessage.text = it
                    binding.assignRoutineStartDateErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.assignRoutineStartDateErrorMessage.text = ""
                    binding.assignRoutineStartDateErrorMessage.visibility = View.GONE
                }
            } ?: run {
                binding.assignRoutineStartDateErrorMessage.text = ""
                binding.assignRoutineStartDateErrorMessage.visibility = View.GONE
            }
        })
    }

    private fun observeDialogState() {
        viewModel.closeAssignDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    dismiss()
                    viewModel.assignDialogClosed()
                }
            }
        })
    }
}
