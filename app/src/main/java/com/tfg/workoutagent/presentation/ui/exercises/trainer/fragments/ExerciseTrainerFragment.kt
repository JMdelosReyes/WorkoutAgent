package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

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
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.domain.exerciseUseCases.ListExercisesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.trainer.adapters.ExerciseListAdapter
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.ListExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.ListExerciselViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_trainer_exercise.*
import kotlinx.android.synthetic.main.fragment_trainer_exercise.shimmer_view_container

class ExerciseTrainerFragment : Fragment() {

    private lateinit var adapter: ExerciseListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this, ListExerciselViewModelFactory(
                ListExercisesUseCaseImpl(ExerciseRepositoryImpl())
            )
        ).get(ListExerciseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ExerciseListAdapter(this.context!!)

        recyclerView.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView.adapter = adapter

        observeData()
    }

    private fun observeData() {
        //si es en un fragmento ponemos viewLifecycleowner como primer parametro

        viewModel.exerciseList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    shimmer_view_container.startShimmer()


                }
                is Resource.Success -> {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmer()
                    adapter.setListData(result.data)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmer()
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
