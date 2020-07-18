package com.tfg.workoutagent.presentation.ui.goals.customer.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.GoalRepositoryImpl
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.goals.customer.adapters.GoalsCustomerListAdapter
import com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels.ListGoalCustomerViewModel
import com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels.ListGoalCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_list_goals_customer.*

class ListGoalCustomerFragment : Fragment() {

    private lateinit var adapterGoals : GoalsCustomerListAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this, ListGoalCustomerViewModelFactory(
                ListGoalCustomerUseCaseImpl(GoalRepositoryImpl())
            )
        ).get(ListGoalCustomerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_goals_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        val darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        adapterGoals = GoalsCustomerListAdapter(this.context!!, darkMode)
        recyclerView_goals_customer.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_goals_customer.adapter = adapterGoals
        observeData()
    }

    private fun setupUI(){
        fab_add_goal.setOnClickListener { findNavController().navigate(ListGoalCustomerFragmentDirections.actionListGoalCustomerFragmentToCreateGoalCustomerFragment()) }
    }
    private fun observeData(){
        viewModel.goalsList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {sfl_rv_goals_customer.startShimmer()}
                is Resource.Failure -> {sfl_rv_goals_customer.visibility = View.GONE
                    sfl_rv_goals_customer.stopShimmer()
                    Toast.makeText(this.context!!,"Something went wrong ${it.exception.message}",Toast.LENGTH_LONG).show()}
                is Resource.Success -> {
                    sfl_rv_goals_customer.visibility = View.GONE
                    sfl_rv_goals_customer.stopShimmer()
                    if(it.data.size==0){
                        //TODO: Mensaje tipo "You don't have any goals yet"
                    }
                    adapterGoals.setListData(it.data)
                    adapterGoals.notifyDataSetChanged()
                }
            }
        })
    }
}
