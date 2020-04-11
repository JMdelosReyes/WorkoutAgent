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
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.databinding.CreateRoutineFragmentBinding
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DayListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModelFactory
import kotlinx.android.synthetic.main.create_routine_fragment.*

class CreateRoutineFragment : Fragment() {

    companion object {
        fun newInstance() = CreateRoutineFragment()
    }

    private val clearData by lazy { CreateRoutineFragmentArgs.fromBundle(arguments!!).clearData }
    private lateinit var adapter: DayListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            activity!!, CreateRoutineViewModelFactory(
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
            //0 -> this.viewModel.clearData()
            1 -> this.viewModel.clearAllData()
            2 -> this.viewModel.clearDayData()
            3 -> this.viewModel.clearActivityData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DayListAdapter(this.context!!)
        recyclerViewRoutineNewDay.layoutManager = LinearLayoutManager(this.context!!)
        recyclerViewRoutineNewDay.adapter = adapter
        observeDayData()
        observeData()
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
