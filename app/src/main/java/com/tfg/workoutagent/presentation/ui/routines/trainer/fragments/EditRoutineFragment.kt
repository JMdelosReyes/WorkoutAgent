package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.os.Bundle
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
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DayListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.EditRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.EditRoutineViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_edit_routine.*
import java.util.*

class EditRoutineFragment : Fragment() {

    private val routineId by lazy { EditRoutineFragmentArgs.fromBundle(arguments!!).routineId }
    private val clearData by lazy { EditRoutineFragmentArgs.fromBundle(arguments!!).clearData }
    private val viewModel by lazy {
        ViewModelProvider(
            activity!!,
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

        adapter = DayListAdapter(this.context!!, {
            viewModel.onEditDay(it)
            findNavController().navigate(
                EditRoutineFragmentDirections.actionEditRoutineFragmentToEditDayEditRoutineFragment(
                    routineId = routineId
                )
            )
        }, {
            viewModel.onDeleteDay(it)
            adapter.notifyDataSetChanged()
        })
        recyclerView_Routine_edit_Day.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_Routine_edit_Day.adapter = adapter

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
        viewModel.routine.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO
                }
                is Resource.Success -> {
                    // TODO
                }
                is Resource.Failure -> {
                    // TODO
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
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
        //TODO Hay que cambiar el valor que entra
        val builder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time)
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        //builder.setSelection()
        val picker: MaterialDatePicker<*> = builder.build()
        routine_startDate_input_edit.setOnClickListener {
            picker.show(activity!!.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
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
