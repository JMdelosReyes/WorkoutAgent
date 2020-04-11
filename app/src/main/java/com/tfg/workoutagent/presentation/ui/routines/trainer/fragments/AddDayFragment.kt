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
import com.tfg.workoutagent.databinding.AddDayFragmentBinding
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.ActivityListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModelFactory
import kotlinx.android.synthetic.main.add_day_fragment.*

class AddDayFragment : Fragment() {

    companion object {
        fun newInstance() = AddDayFragment()
    }

    private lateinit var adapter: ActivityListAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            activity!!, CreateRoutineViewModelFactory(
                ManageRoutineUseCaseImpl(RoutineRepositoryImpl(), ExerciseRepositoryImpl())
            )
        ).get(CreateRoutineViewModel::class.java)
    }

    private lateinit var binding: AddDayFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_day_fragment,
            container,
            false
        )
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActivityListAdapter(this.context!!)
        recycler_add_day_activities.layoutManager = LinearLayoutManager(this.context!!)
        recycler_add_day_activities.adapter = adapter

        // TODO
        viewModel.adapter = adapter

        observeData()

        setupButtons()
    }

    private fun observeData() {
        viewModel.activities.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.setListData(it)
                adapter.notifyDataSetChanged()
                // findNavController().navigate(CreateRoutineFragmentDirections.actionCreateRoutineToAddDayFragment())
            }
        })
    }

    private fun setupButtons() {
        save_create_day_button.setOnClickListener {
            viewModel.onSaveDay()
            findNavController().navigate(
                AddDayFragmentDirections.actionAddDayFragmentToCreateRoutine(
                    2
                )
            )
        }

        add_day_activity_button.setOnClickListener {
            findNavController().navigate(AddDayFragmentDirections.actionAddDayFragmentToAddActivityFragment2())
        }

        cancel_create_day_button.setOnClickListener {
            findNavController().navigate(
                AddDayFragmentDirections.actionAddDayFragmentToCreateRoutine(
                    clearData = 2
                )
            )
        }
    }
}
