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
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DayListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.DisplayRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.DisplayRoutineViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.display_routine_fragment.*

class DisplayRoutineFragment : Fragment() {

    private val routineId by lazy { DisplayRoutineFragmentArgs.fromBundle(arguments!!).routineId }
    private val routineTitle by lazy { DisplayRoutineFragmentArgs.fromBundle(arguments!!).routineTitle }
    private lateinit var adapter: DayListAdapter


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DisplayRoutineViewModelFactory(
                routineId,
                ManageRoutineUseCaseImpl(RoutineRepositoryImpl(), ExerciseRepositoryImpl())
            )
        ).get(DisplayRoutineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.display_routine_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()

        adapter = DayListAdapter(this.context!!)
        recyclerViewRoutineDay.layoutManager = LinearLayoutManager(this.context!!)
        recyclerViewRoutineDay.adapter = adapter
        observeData()
    }

    private fun setupButtons() {
        edit_routine_button.setOnClickListener {
            findNavController().navigate(
               DisplayRoutineFragmentDirections.actionDisplayRoutineToEditRoutineFragment(routineId)
            )
        }
    }

    private fun observeData() {
        //si es en un fragmento ponemos viewLifecycleowner como primer parametro

        viewModel.routine.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    shimmer_view_container_routine.startShimmer()
                    edit_routine_button.visibility = View.INVISIBLE
                }
                is Resource.Success -> {
                    shimmer_view_container_routine.visibility = View.GONE
                    shimmer_view_container_routine.stopShimmer()
                    edit_routine_button.visibility = View.VISIBLE
                    adapter.setListData(result.data.days)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    shimmer_view_container_routine.visibility = View.GONE
                    shimmer_view_container_routine.stopShimmer()
                    Toast.makeText(
                        this.context!!,
                        "Ocurrió un error ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
