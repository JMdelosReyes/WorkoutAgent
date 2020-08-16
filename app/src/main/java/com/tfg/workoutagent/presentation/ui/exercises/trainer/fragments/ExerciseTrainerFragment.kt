package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.os.Bundle
import android.util.Log
import android.view.*

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tfg.workoutagent.R
import com.tfg.workoutagent.TrainerActivity
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.domain.exerciseUseCases.ListExercisesUseCaseImpl
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.presentation.ui.exercises.trainer.adapters.ExerciseListAdapter
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.ListExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.ListExerciseViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_trainer_exercise.*

class ExerciseTrainerFragment : Fragment() {

    private lateinit var adapter: ExerciseListAdapter
    private lateinit var completeExerciseList: MutableList<Exercise>
    private val viewModel by lazy {
        ViewModelProvider(
            this, ListExerciseViewModelFactory(
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

    //****************************************************************
    //Código necesario para usar el filtro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater?.inflate(R.menu.search_menu, menu)
        val searchView =
            SearchView((context as TrainerActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val lowerText = query.toLowerCase()
                if (lowerText == "") {
                    viewModel.filteredExerciseList = completeExerciseList
                    adapter.setListData(viewModel.filteredExerciseList)
                    adapter.notifyDataSetChanged()
                } else {
                    var list = mutableListOf<Exercise>()
                    var alreadyAdded = false
                    for (exercise in completeExerciseList) {
                        for (tag in exercise.tags) {
                            if (tag.toLowerCase().contains(lowerText)) {
                                list.add(exercise)
                                alreadyAdded = true
                                break
                            }
                        }
                        if (!alreadyAdded) {
                            if (exercise.title.toLowerCase()
                                    .contains(lowerText) || exercise.description.toLowerCase()
                                    .contains(lowerText)
                            ) {
                                list.add(exercise)
                            }
                        }

                    }
                    viewModel.filteredExerciseList = list
                    adapter.setListData(viewModel.filteredExerciseList)
                    adapter.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val lowerText = newText.toLowerCase()
                if (lowerText == "") {
                    viewModel.filteredExerciseList = completeExerciseList
                    adapter.setListData(viewModel.filteredExerciseList)
                    adapter.notifyDataSetChanged()
                } else {
                    var list = mutableListOf<Exercise>()
                    var alreadyAdded = false
                    for (exercise in completeExerciseList) {
                        for (tag in exercise.tags) {
                            if (tag.toLowerCase().contains(lowerText)) {
                                list.add(exercise)
                                alreadyAdded = true
                                break
                            }
                        }
                        if (!alreadyAdded) {
                            if (exercise.title.toLowerCase()
                                    .contains(lowerText) || exercise.description.toLowerCase()
                                    .contains(lowerText)
                            ) {
                                list.add(exercise)
                            }
                        }

                    }
                    viewModel.filteredExerciseList = list
                    adapter.setListData(viewModel.filteredExerciseList)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }
    //****************************************************************

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()

        adapter = ExerciseListAdapter(this.context!!)
        recyclerView.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView.adapter = adapter
        observeData()
        this.viewModel.reloadExercises()
    }

    private fun observeData() {
        viewModel.exerciseList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    shimmer_view_container.startShimmer()
                }
                is Resource.Success -> {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmer()
                    viewModel.filteredExerciseList = result.data
                    completeExerciseList = result.data
                    adapter.setListData(viewModel.filteredExerciseList)
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

    private fun setupButtons() {
        fab_button.setOnClickListener {
            findNavController().navigate(
                ExerciseTrainerFragmentDirections.actionNavigationExercisesTrainerToCreateExerciseFragment()
            )
        }
    }
}
