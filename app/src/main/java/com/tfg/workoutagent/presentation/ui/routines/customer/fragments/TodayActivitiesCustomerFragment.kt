package com.tfg.workoutagent.presentation.ui.routines.customer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCaseImpl
import com.tfg.workoutagent.models.ActivitySet
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.presentation.ui.routines.customer.adapters.TodayActivitiesCustomerListAdapter
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.TodayActivitiesCustomerViewModel
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.TodayActivitiesCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_today_activities_customer.*

class TodayActivitiesCustomerFragment : BaseFragment() {

    private lateinit var adapterActivities : TodayActivitiesCustomerListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this, TodayActivitiesCustomerViewModelFactory(
                ManageRoutineCustomerUseCaseImpl(
                    RoutineRepositoryImpl()
                )
            )).get(TodayActivitiesCustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today_activities_customer, container, false)
    }

    class ActionListeners(val deleteClickListener : (position: Int,  activity: RoutineActivity) -> Unit,
        val finishedClickListener : (position: Int, repetitions: Int?, weight:Double?,  activity: RoutineActivity) -> String)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterActivities = TodayActivitiesCustomerListAdapter(this.requireContext(),
            completedClickListener@{position: Int, activity: RoutineActivity -> this.viewModel.completedActivity(activity, position)},
            ActionListeners(
            deletedClickListener@{position: Int,  activity: RoutineActivity -> this.viewModel.removeSetActivity(activity, position)},
            finishedClickListener@{position: Int, repetition:Int?, weight: Double?, activity: RoutineActivity -> this.viewModel.updateSetActivityToday(position, repetition!!, weight!!, activity)}))
        recycler_today_activities.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recycler_today_activities.adapter = adapterActivities
        observeData()
    }


    fun observeData(){
        viewModel.routine.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    sv_today.startShimmer()
                }
                is Resource.Failure -> {
                    sv_today.stopShimmer()
                    sv_today.visibility = View.GONE
                    Log.i("Resource.Failure", it.exception.toString())
                }
                is Resource.Success -> {sv_today.stopShimmer()
                    sv_today.visibility = View.GONE
                    val routine = it.data
                    val activities = routine.days[0].activities
                    if(activities.size==0){
                        //TODO: Mensaje tipo "You don't have any goals yet"
                    }
                    adapterActivities.setDataList(activities)
                    adapterActivities.notifyDataSetChanged()
                    this.viewModel.dayLoaded()
                }
            }
        })

    }
}
