package com.tfg.workoutagent.presentation.ui.routines.customer.fragments

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
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.customer.adapters.ListHistoricDaysCustomerAdapter
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.ListHistoricDaysCustomerViewModel
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.ListHistoricDaysCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_list_historic_days.*

class ListHistoricDaysCustomerFragment : Fragment() {

    private lateinit var adapter : ListHistoricDaysCustomerAdapter
    private val routineId by lazy { ListHistoricDaysCustomerFragmentArgs.fromBundle(
        requireArguments()
    ).routineId}

    private val viewModel by lazy { ViewModelProvider(
        this,
        ListHistoricDaysCustomerViewModelFactory(
            routineId,
            HistoricRoutinesUseCaseImpl(RoutineRepositoryImpl())
        )
    ).get(ListHistoricDaysCustomerViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_historic_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        adapter =
            ListHistoricDaysCustomerAdapter(
                routineId,
                this.requireContext()
            )
        rcv_historic_days.layoutManager = LinearLayoutManager(this.requireContext())
        rcv_historic_days.adapter = adapter
    }

    fun observeData(){
        viewModel.daysList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    sv_my_historic_days.startShimmer()
                }
                is Resource.Success -> {
                    sv_my_historic_days.visibility = View.GONE
                    sv_my_historic_days.stopShimmer()
                    if(it.data.days.size == 0){
                        //TODO: Show a message like --> "You have no days assigned! Talk with your trainer to start training!"
                    }else{
                        adapter.setListData(it.data.days)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Failure-> {
                    sv_my_historic_days.visibility = View.GONE
                    sv_my_historic_days.stopShimmer()
                    Toast.makeText(this.requireContext(),"Ocurrió un error ${it.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
