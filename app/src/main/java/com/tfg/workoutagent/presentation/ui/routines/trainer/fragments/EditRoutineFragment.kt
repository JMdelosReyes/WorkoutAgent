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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.fragment_edit_routine.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EditRoutineFragment : Fragment() {

    private val routineId by lazy { EditRoutineFragmentArgs.fromBundle(arguments!!).routineId }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
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
        // Inflate the layout for this fragment

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_routine,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DayListAdapter(this.context!!)
        recyclerView_Routine_edit_Day.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_Routine_edit_Day.adapter = adapter
        val itemTouchHelper = setUpItemTouchHelper()
        itemTouchHelper.attachToRecyclerView(recyclerView_Routine_edit_Day)

        observeData()
        setupButtons()
    }

    private fun setUpItemTouchHelper(): ItemTouchHelper {
        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder as DayListAdapter.DayListViewHolder
                val day = viewHolder.day
                viewModel.removeDay(day)
                adapter.notifyDataSetChanged()
            }
        }

        return ItemTouchHelper(simpleItemTouchCallback)
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
        viewModel.days.observe(viewLifecycleOwner, Observer {
            adapter.setListData(viewModel.days.value!!)
            adapter.notifyDataSetChanged()
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
}
