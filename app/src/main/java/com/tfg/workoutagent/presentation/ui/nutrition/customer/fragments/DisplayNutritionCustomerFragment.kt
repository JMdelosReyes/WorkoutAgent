package com.tfg.workoutagent.presentation.ui.nutrition.customer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentNutritionCustomerBinding
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.presentation.ui.nutrition.customer.viewModels.DisplayNutritionCustomerViewModel
import com.tfg.workoutagent.presentation.ui.nutrition.customer.viewModels.DisplayNutritionCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_nutrition_customer.*
import java.lang.Thread.sleep


class DisplayNutritionCustomerFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this,
        DisplayNutritionCustomerViewModelFactory(
            DisplayProfileUserUseCaseImpl(UserRepositoryImpl())
        )
    ).get(
        DisplayNutritionCustomerViewModel::class.java)}
    private lateinit var binding: FragmentNutritionCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_nutrition_customer,
            container,
            false
        )
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return  this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it is Resource.Success){
                    viewModel.loadData(it)
                }
            }
        })

        viewModel.calories.observe(viewLifecycleOwner, Observer {
            daily_calories_customer.text = viewModel.calories.value
        })

        viewModel.proteins.observe(viewLifecycleOwner, Observer {
            protein_column.text = viewModel.proteins.value
        })

        viewModel.fats.observe(viewLifecycleOwner, Observer {
            fat_column.text = viewModel.fats.value
        })

        viewModel.carbohydrates.observe(viewLifecycleOwner, Observer {
            carbohydrate_column.text = viewModel.carbohydrates.value
        })






    }
}
