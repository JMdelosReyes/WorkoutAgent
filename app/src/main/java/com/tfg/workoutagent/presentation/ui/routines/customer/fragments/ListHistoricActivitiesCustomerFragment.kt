package com.tfg.workoutagent.presentation.ui.routines.customer.fragments

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
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.customer.fragments.ListHistoricActivitiesCustomerFragmentArgs
import com.tfg.workoutagent.presentation.ui.routines.customer.adapters.ListHistoricActivitiesCustomerAdapter
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.ListHistoricActivitiesCustomerViewModel
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.ListHistoricActivitiesCustomerViewModelFactory
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.ListHistoricActivitiesAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.ListHistoricActivitiesViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.ListHistoricActivitiesViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_list_historic_activities.*

class ListHistoricActivitiesCustomerFragment : Fragment() {

    private val routineId by lazy { ListHistoricActivitiesCustomerFragmentArgs.fromBundle(
        requireArguments()
    ).routineId}
    private val numDay by lazy { ListHistoricActivitiesCustomerFragmentArgs.fromBundle(
        requireArguments()
    ).numDay}
    private val nameDay by lazy { ListHistoricActivitiesCustomerFragmentArgs.fromBundle(
        requireArguments()
    ).nameDay}

    private lateinit var adapter : ListHistoricActivitiesCustomerAdapter

    private val viewModel by lazy { ViewModelProvider(
        this,
        ListHistoricActivitiesCustomerViewModelFactory(
            routineId,
            numDay,
            HistoricRoutinesUseCaseImpl(RoutineRepositoryImpl())
        )
    ).get(ListHistoricActivitiesCustomerViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_historic_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            ListHistoricActivitiesCustomerAdapter(
                requireContext()
            )
        rcv_historic_activities.layoutManager =  LinearLayoutManager(this.requireContext())
        rcv_historic_activities.adapter = adapter
        observeData()
    }
    fun observeData(){
        viewModel.activityList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    sv_my_historic_activities.startShimmer()
                }
                is Resource.Success -> {
                    sv_my_historic_activities.visibility = View.GONE
                    sv_my_historic_activities.stopShimmer()
                    if(it.data.days[numDay].activities.size == 0){
                        //TODO: Show a message like --> "You have no activities! Talk with your trainer to start training!"
                    }else{
                        adapter.setListData(it.data.days[numDay].activities)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Failure-> {
                    sv_my_historic_activities.visibility = View.GONE
                    sv_my_historic_activities.stopShimmer()
                    Toast.makeText(this.requireContext(),"Ocurrió un error ${it.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
