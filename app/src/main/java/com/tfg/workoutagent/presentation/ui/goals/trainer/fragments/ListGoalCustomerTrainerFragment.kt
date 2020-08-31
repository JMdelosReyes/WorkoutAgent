package com.tfg.workoutagent.presentation.ui.goals.trainer.fragments

import android.content.res.Configuration
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
import com.tfg.workoutagent.data.repositoriesImpl.GoalRepositoryImpl
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.goals.trainer.adapters.GoalsCustomerTrainerListAdapter
import com.tfg.workoutagent.presentation.ui.goals.trainer.viewmodels.ListGoalCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.goals.trainer.viewmodels.ListGoalCustomerTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_list_goal_customer_trainer.*

class ListGoalCustomerTrainerFragment : Fragment() {

    private lateinit var adapterGoals : GoalsCustomerTrainerListAdapter
    private val customerId by lazy { ListGoalCustomerTrainerFragmentArgs.fromBundle(arguments!!).customerId}

    private val viewModel by lazy {
        ViewModelProvider(
            this, ListGoalCustomerTrainerViewModelFactory(customerId,
                ListGoalCustomerTrainerUseCaseImpl(GoalRepositoryImpl())
            )
        ).get(ListGoalCustomerTrainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_goal_customer_trainer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        adapterGoals = GoalsCustomerTrainerListAdapter(this.context!!, darkMode)
        recyclerView_goals_trainer.layoutManager = LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL, false)
        recyclerView_goals_trainer.adapter = adapterGoals
        observeData()
    }
    private fun observeData() {
        viewModel.goalsList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    sfl_rv_goals_trainer.startShimmer()
                }
                is Resource.Failure -> {
                    sfl_rv_goals_trainer.visibility = View.GONE
                    sfl_rv_goals_trainer.stopShimmer()
                    Toast.makeText(
                        this.context!!, "Something went wrong ${it.exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Success -> {
                    sfl_rv_goals_trainer.visibility = View.GONE
                    sfl_rv_goals_trainer.stopShimmer()
                    if (it.data.size == 0) {
                        //TODO: Mensaje tipo "You don't have any goals yet"
                    }
                    adapterGoals.setListData(it.data)
                    adapterGoals.notifyDataSetChanged()
                }
            }
        })
    }
}
