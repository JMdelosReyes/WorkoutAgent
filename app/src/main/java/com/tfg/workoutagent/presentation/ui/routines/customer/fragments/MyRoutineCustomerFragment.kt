package com.tfg.workoutagent.presentation.ui.routines.customer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineCustomerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.customer.adapters.MyRoutineDaysListAdapter
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.MyRoutineCustomerViewModel
import com.tfg.workoutagent.presentation.ui.routines.customer.viewModels.MyRoutineCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_my_routine_customer.*

class MyRoutineCustomerFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MyRoutineCustomerViewModelFactory(ManageRoutineCustomerUseCaseImpl(RoutineRepositoryImpl()))
        ).get(MyRoutineCustomerViewModel::class.java)
    }
    private lateinit var adapter : MyRoutineDaysListAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_routine_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyRoutineDaysListAdapter(this.requireContext(), super.getDarkMode())
        rcv_days.layoutManager = LinearLayoutManager(this.requireContext())
        rcv_days.adapter = adapter
        observeData()
    }

    private fun observeData(){
        viewModel.routine.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> { sv_my_routine.startShimmer()}
                is Resource.Success -> { sv_my_routine.visibility = View.GONE
                    sv_my_routine.stopShimmer()
                    if(it.data.days.size == 0 || it.data.id == "DEFAULT_ID"){
                        //TODO: Show a message like --> "You have no routine assigned! Talk with your trainer to start training!"
                    }else{
                        adapter.setDataList(it.data.days)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Resource.Failure -> {
                    sv_my_routine.visibility = View.GONE
                    sv_my_routine.stopShimmer()
                    Toast.makeText(this.requireContext(),"Ocurrió un error ${it.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
