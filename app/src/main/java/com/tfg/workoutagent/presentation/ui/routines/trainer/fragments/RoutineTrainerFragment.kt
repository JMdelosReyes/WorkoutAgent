package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.domain.routineUseCases.ListRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.RoutineListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.ListRoutineTrainerViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.ListRoutineTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_trainer_routine.*

class RoutineTrainerFragment : Fragment() {

    private lateinit var adapter: RoutineListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ListRoutineTrainerViewModelFactory(
                ListRoutinesUseCaseImpl(RoutineRepositoryImpl())
            )
        ).get(ListRoutineTrainerViewModel::class.java)
    }

    private val fabActions = object {
        var currentMode = 0 // 0: Add routine, 1: Assign routine

        fun addRoutine() {
            findNavController().navigate(
                RoutineTrainerFragmentDirections.actionNavigationRoutineTrainerToCreateRoutineFragment(
                    clearData = 1
                )
            )
        }

        fun assignRoutine() {
            findNavController().navigate(RoutineTrainerFragmentDirections.actionNavigationRoutineTrainerToAssignRoutineFragment())
        }

        fun doAction() {
            if (this.currentMode == 0) {
                this.addRoutine()
            } else {
                this.assignRoutine()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupTabs()

        adapter = RoutineListAdapter(this.requireContext())
        recyclerViewRoutine.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerViewRoutine.adapter = adapter
        observeData()
    }

    private fun observeData() {
        viewModel.routineList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    shimmer_view_container_routine.startShimmer()
                }
                is Resource.Success -> {
                    shimmer_view_container_routine.visibility = View.GONE
                    shimmer_view_container_routine.stopShimmer()
                    adapter.setListData(this.viewModel.getGeneralRoutines())
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    shimmer_view_container_routine.visibility = View.GONE
                    shimmer_view_container_routine.stopShimmer()
                    Toast.makeText(
                        this.requireContext(),
                        "Ocurrió un error ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupButtons() {
        fab_button_routine.setOnClickListener {
            fabActions.doAction()
        }
    }

    private fun setupTabs() {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // nothing
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // nothing
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (it.position == 0) {
                        fabActions.currentMode = 0
                        adapter.setListData(viewModel.getGeneralRoutines())
                        adapter.notifyDataSetChanged()
                    } else {
                        fabActions.currentMode = 1
                        adapter.setListData(viewModel.getAssignedRoutines())
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}

