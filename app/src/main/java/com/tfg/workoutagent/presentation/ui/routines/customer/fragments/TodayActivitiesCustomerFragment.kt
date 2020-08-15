package com.tfg.workoutagent.presentation.ui.routines.customer.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCaseImpl
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

    class ActionListeners(val deleteClickListener : (activityPos: Int, position: Int) -> Unit,
        val finishedClickListener : (activityPos: Int, setPosition: Int, repetitions: Int?, weight:Double?) -> String)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterActivities = TodayActivitiesCustomerListAdapter(this.requireContext(),
            completedClickListener@{position: Int -> this.viewModel.completedActivity(position) },
            ActionListeners(
            deletedClickListener@{activityPos: Int, position: Int -> this.viewModel.removeSetActivity(activityPos, position)},
            finishedClickListener@{activityPos: Int, setPosition: Int, repetition:Int?, weight: Double? ->
                this.viewModel.updateSetActivityToday(activityPos, setPosition, repetition, weight)})
            )
        recycler_today_activities.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recycler_today_activities.adapter = adapterActivities
        setupUI()
        observeData()
    }

    fun setupUI(){
        finish_this_day_button.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Finish this day")
            builder.setMessage("If you finish this day, activities and its values cannot be edited. Are you sure?")

            builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                this.viewModel.finishDay()
                dialog.dismiss()
            }

            builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        }
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
                    Log.i("it", it.toString())
                }
                is Resource.Success -> {sv_today.stopShimmer()
                    sv_today.visibility = View.GONE
                    var activities = mutableListOf<RoutineActivity>()
                    for (day in it.data.days) {
                        if (!day.completed) {
                            activities = day.activities
                            break
                        }
                    }
                    if(activities.size==0){
                        message_no_routine.text = "Right now, you do not have any planned activity to perform. Contact your trainer to continue your training."
                    }else{
                        finish_this_day_button.visibility = View.VISIBLE
                    }
                    adapterActivities.setDataList(activities)
                    adapterActivities.notifyDataSetChanged()
                    this.viewModel.dayLoaded()
                }
            }
        })
        viewModel.dayLoaded.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    viewModel.dayLoaded()
                }
                false -> {
                    viewModel.dayLoaded()
                }
            }
        })
        viewModel.finishedDay.observe(viewLifecycleOwner, Observer {
            when (it){
                true -> {
                    sv_today.startShimmer()
                    findNavController().navigate(TodayActivitiesCustomerFragmentDirections.actionNavigationTodayCustomerSelf())
                    sv_today.stopShimmer()
                    sv_today.visibility = View.GONE
                }
                false -> {
                    Log.i("finishedDay", "Something went wrong")
                }
            }
        })

    }
}
