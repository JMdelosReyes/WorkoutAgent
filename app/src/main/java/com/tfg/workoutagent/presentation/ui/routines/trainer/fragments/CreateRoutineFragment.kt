package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tfg.workoutagent.databinding.CreateRoutineFragmentBinding
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DayListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModelFactory
import com.tfg.workoutagent.vo.createAlertDialog
import kotlinx.android.synthetic.main.create_routine_fragment.*

class CreateRoutineFragment : Fragment() {

    companion object {
        fun newInstance() = CreateRoutineFragment()
    }

    private val clearData by lazy { CreateRoutineFragmentArgs.fromBundle(requireArguments()).clearData }
    private lateinit var adapter: DayListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(), CreateRoutineViewModelFactory(
                ManageRoutineUseCaseImpl(RoutineRepositoryImpl(), ExerciseRepositoryImpl())
            )
        ).get(CreateRoutineViewModel::class.java)
    }

    private lateinit var binding: CreateRoutineFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.create_routine_fragment,
            container,
            false
        )
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this


        when (clearData) {
            // 0 -> this.viewModel.clearData()
            1 -> this.viewModel.clearAllData()
            2 -> this.viewModel.clearDayData()
            3 -> this.viewModel.clearActivityData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            DayListAdapter(this.requireContext(), {
                viewModel.onEditDay(it)
                findNavController().navigate(CreateRoutineFragmentDirections.actionCreateRoutineToEditDayFragment())
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
        recyclerViewRoutineNewDay.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerViewRoutineNewDay.adapter = adapter

        observeDayData()
        observeData()
        observeErrors()
        setupButtons()
    }

    private fun observeErrors() {
        viewModel.titleError.observe(viewLifecycleOwner, Observer {
            binding.routineTitleInputEdit.error =
                if (it != "") it else null
        })

        /*viewModel.startDateError.observe(viewLifecycleOwner, Observer {
            binding.routineStartDateInputEdit.error =
                if (it != "") it else null
        })*/

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

    private fun setupButtons() {
        /*val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.pickerDate.value!!.time.toLong())
        //builder.setSelection()
        val picker: MaterialDatePicker<*> = builder.build()
        routine_startDate_input_edit.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.setDate(it as Long)
            }
        }*/
    }


    private fun observeData() {
        viewModel.addDay.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    adapter.setListData(viewModel.days.value!!)
                    adapter.notifyDataSetChanged()
                    findNavController().navigate(CreateRoutineFragmentDirections.actionCreateRoutineToAddDayFragment())
                    viewModel.addDayNavigationCompleted()
                }
            }
        })

        viewModel.routineCreated.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(CreateRoutineFragmentDirections.actionCreateRoutineToNavigationRoutineTrainer2())
                }
            }
        })
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
