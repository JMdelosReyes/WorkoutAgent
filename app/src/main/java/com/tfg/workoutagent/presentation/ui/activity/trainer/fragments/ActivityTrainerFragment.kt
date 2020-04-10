package com.tfg.workoutagent.presentation.ui.activity.trainer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.domain.routineUseCases.ActivityTimelineRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.activity.trainer.adapters.ActivityTimelineAdapter
import com.tfg.workoutagent.presentation.ui.activity.trainer.viewModels.ActivityTimelineTrainerViewModel
import com.tfg.workoutagent.presentation.ui.activity.trainer.viewModels.ActivityTimelineTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_trainer_activity.*

class ActivityTrainerFragment : Fragment() {

    private lateinit var adapter: ActivityTimelineAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ActivityTimelineTrainerViewModelFactory(
                ActivityTimelineRoutinesUseCaseImpl(RoutineRepositoryImpl())
            )
        ).get(ActivityTimelineTrainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupButtons()

        adapter = ActivityTimelineAdapter(this.context!!)
        recyclerView_activity_timeline.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_activity_timeline.adapter = adapter
        observeData()
    }

    private fun observeData() {
        //si es en un fragmento ponemos viewLifecycleowner como primer parametro

        viewModel.activityList.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    shimmer_view_container_acitivity_timeline.startShimmer()
                }
                is Resource.Success -> {
                    shimmer_view_container_acitivity_timeline.visibility = View.GONE
                    shimmer_view_container_acitivity_timeline.stopShimmer()
                    adapter.setListData(result.data)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {
                    shimmer_view_container_acitivity_timeline.visibility = View.GONE
                    shimmer_view_container_acitivity_timeline.stopShimmer()
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
