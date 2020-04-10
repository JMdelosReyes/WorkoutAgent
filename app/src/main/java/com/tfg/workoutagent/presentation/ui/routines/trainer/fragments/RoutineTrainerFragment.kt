package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupButtons()

        adapter = RoutineListAdapter(this.context!!)
        recyclerViewRoutine.layoutManager = LinearLayoutManager(this.context!!)
        recyclerViewRoutine.adapter = adapter
        observeData()
    }

    private fun observeData() {
        //si es en un fragmento ponemos viewLifecycleowner como primer parametro

        viewModel.routineList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    shimmer_view_container_routine.startShimmer()
                }
                is Resource.Success -> {
                    shimmer_view_container_routine.visibility = View.GONE
                    shimmer_view_container_routine.stopShimmer()
                    adapter.setListData(result.data)
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

   /* private fun setupButtons() {
        fab_button.setOnClickListener {
            findNavController().navigate(
                ExerciseTrainerFragmentDirections.actionNavigationExercisesTrainerToCreateExerciseFragment()
            )
        }
    }*/
}

