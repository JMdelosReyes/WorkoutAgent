package com.tfg.workoutagent.presentation.ui.users.trainer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.userUseCases.DisplayCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.DisplayCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.users.trainer.viewModels.DisplayCustomerTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.activity_bottom_navigation_trainer.*
import kotlinx.android.synthetic.main.fragment_display_customer_trainer.*

class DisplayCustomerTrainerFragment : Fragment() {

    private val customerId by lazy { DisplayCustomerTrainerFragmentArgs.fromBundle(arguments!!).customerId}
    private val customerName by lazy { DisplayCustomerTrainerFragmentArgs.fromBundle(arguments!!).customerName }

    private val viewModel by lazy {
        ViewModelProvider(
            this, DisplayCustomerTrainerViewModelFactory(
                customerId,
                DisplayCustomerTrainerUseCaseImpl(UserRepositoryImpl())
            )
        ).get(DisplayCustomerTrainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_customer_trainer, container, false)
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupUI()
    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    //TODO: showProgress()
                }
                is Resource.Success -> {
                    //TODO: hideProgress()
                    display_customer_name.text = it.data.name + " " + it.data.surname
                    display_customer_birthday.text = parseDateToFriendlyDate(it.data.birthday)
                    display_customer_email.text = it.data.email
                    display_customer_phone.text = it.data.phone
                    if(it.data.genre == "M"){
                        display_customer_gender.text = getString(R.string.genre_male)
                    }else{
                        display_customer_gender.text = getString(R.string.genre_female)
                    }
                    display_customer_height.text = it.data.height.toString() + " cm"
                    display_customer_weight.text = it.data.weights[it.data.weights.lastIndex].weight.toString() + " kg"

                    if(it.data.photo == "" || it.data.photo == "DEFAULT_IMAGE"){
                        Glide.with(this).load(R.drawable.ic_person_black_60dp).into(circleImageViewCustomer)
                    }else{
                        Glide.with(this).load(it.data.photo).into(circleImageViewCustomer)
                    }
                }
                is Resource.Failure -> {
                    //TODO: hideProgress()
                    Toast.makeText(context, "${it}", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun setupUI(){
        display_customer_button_edit.setOnClickListener {
            findNavController().navigate(DisplayCustomerTrainerFragmentDirections.actionDisplayCustomerToEditDeleteCustomerTrainerFragment(
                customerId, customerName
            ))
        }

        display_customer_button_nutrition.setOnClickListener {
            findNavController().navigate(DisplayCustomerTrainerFragmentDirections.actionDisplayCustomerToNutritionCustomerTrainerFragment(customerId))
        }
    }
}
