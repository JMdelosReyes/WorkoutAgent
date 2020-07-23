package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditRoutineBindingImpl
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DayListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.EditRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.EditRoutineViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.createAlertDialog
import kotlinx.android.synthetic.main.fragment_edit_routine.*
import java.util.*

class EditRoutineFragment : Fragment() {

    private val routineId by lazy { EditRoutineFragmentArgs.fromBundle(requireArguments()).routineId }
    private val clearData by lazy { EditRoutineFragmentArgs.fromBundle(requireArguments()).clearData }
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            EditRoutineViewModelFactory(
                routineId,
                ManageRoutineUseCaseImpl(RoutineRepositoryImpl(), ExerciseRepositoryImpl())
            )
        ).get(EditRoutineViewModel::class.java)
    }
    private lateinit var binding: FragmentEditRoutineBindingImpl
    private lateinit var adapter: DayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_routine,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        when (clearData) {
            0 -> this.viewModel.clearNothing()
            1 -> this.viewModel.clearAllData()
            2 -> this.viewModel.clearDayData()
            3 -> this.viewModel.clearActivityData()
        }

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DayListAdapter(this.requireContext(), {
            viewModel.onEditDay(it)
            findNavController().navigate(
                EditRoutineFragmentDirections.actionEditRoutineFragmentToEditDayEditRoutineFragment(
                    routineId = routineId
                )
            )
        }, {
            createAlertDialog(
                requireContext(),
                "Delete day",
                "Are you sure you want to delete this day?",
                {
                    viewModel.onDeleteDay(it)
                    adapter.notifyDataSetChanged()
                },
                {})
        })
        recyclerView_Routine_edit_Day.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView_Routine_edit_Day.adapter = adapter

        this.viewModel.updateRoutineId(this.routineId)
        this.viewModel.loadRoutine()

        observeData()
        observeErrors()
        observeDayData()
        setupButtons()
    }

    private fun observeErrors() {
        viewModel.titleError.observe(viewLifecycleOwner, Observer {
            binding.routineTitleInputEdit.error =
                if (it != "") it else null
        })

        viewModel.startDateError.observe(viewLifecycleOwner, Observer {
            binding.routineStartDateInputEdit.error =
                if (it != "") it else null
        })

        viewModel.daysError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == "") {
                    binding.routineDaysError.visibility = View.INVISIBLE
                } else {
                    binding.routineDaysError.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        viewModel.routineLoaded.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    this.viewModel.routineLoaded()
                }
            }
        })

        viewModel.routineDeleted.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Toast.makeText(context, "Routine deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditRoutineFragmentDirections.actionEditRoutineFragmentToNavigationRoutineTrainer())
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.routineSaved.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Toast.makeText(context, "Routine updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditRoutineFragmentDirections.actionEditRoutineFragmentToNavigationRoutineTrainer())
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.addDay.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    adapter.setListData(viewModel.days.value!!)
                    adapter.notifyDataSetChanged()
                    findNavController().navigate(
                        EditRoutineFragmentDirections.actionEditRoutineFragmentToAddDayEditRoutineFragment(
                            routineId
                        )
                    )
                    viewModel.addDayNavigationCompleted()
                }
            }
        })
    }

    private fun setupButtons() {
        // TODO Hay que cambiar el valor que entra
        val builder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time)
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        // builder.setSelection()
        val picker: MaterialDatePicker<*> = builder.build()
        routine_startDate_input_edit.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
        }

        delete_routine_button.setOnClickListener {
            createAlertDialog(
                requireContext(),
                "Delete this routine",
                "Are you sure you want to delete this routine?",
                { viewModel.onDelete() },
                {})
        }
    }

    private fun observeDayData() {
        viewModel.days.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.setListData(viewModel.days.value!!)
                adapter.notifyDataSetChanged()
            }
        })
    }
}
