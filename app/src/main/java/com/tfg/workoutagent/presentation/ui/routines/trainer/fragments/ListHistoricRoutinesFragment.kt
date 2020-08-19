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
import com.tfg.workoutagent.domain.routineUseCases.HistoricRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.ListHistoricRoutinesAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.ListHistoricRoutinesViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.ListHistoricRoutinesViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_list_historic_routines.*

class ListHistoricRoutinesFragment : Fragment() {

    private lateinit var adapter : ListHistoricRoutinesAdapter
    private val customerId by lazy { ListHistoricRoutinesFragmentArgs.fromBundle(
        requireArguments()
    ).customerId}

    private val viewModel by lazy {
        ViewModelProvider(
        this,
            ListHistoricRoutinesViewModelFactory(
                customerId, HistoricRoutinesUseCaseImpl(RoutineRepositoryImpl())
            )
    ).get(ListHistoricRoutinesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_historic_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        adapter =
            ListHistoricRoutinesAdapter(
                this.requireContext()
            )
        rcv_historic_routines.layoutManager = LinearLayoutManager(this.requireContext())
        rcv_historic_routines.adapter = adapter
        observeData()
    }

    private fun setupUI(){

    }

    private fun observeData(){
        viewModel.routineList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    sv_my_historic_routines.startShimmer()
                }
                is Resource.Success -> {
                    sv_my_historic_routines.visibility = View.GONE
                    sv_my_historic_routines.stopShimmer()
                    if(it.data.size == 0){
                        //TODO: Show a message like --> "You have no routines assigned! Talk with your trainer to start training!"
                    }else{
                        adapter.setListData(it.data)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Failure-> {
                    sv_my_historic_routines.visibility = View.GONE
                    sv_my_historic_routines.stopShimmer()
                    Toast.makeText(this.requireContext(),"Ocurrió un error ${it.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
