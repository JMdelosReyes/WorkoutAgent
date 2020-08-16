package com.tfg.workoutagent.presentation.ui.nutrition.trainer.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentNutritionCustomerTrainerBinding
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.nutrition.trainer.viewModels.EditNutritionCustomerTrainerViewModel
import com.tfg.workoutagent.presentation.ui.nutrition.trainer.viewModels.EditNutritionCustomerTrainerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_nutrition_customer_trainer.*


class NutritionCustomerTrainerFragment : Fragment() {

    private val customerId by lazy { NutritionCustomerTrainerFragmentArgs.fromBundle(arguments!!).customerId}
    private val viewModel by lazy { ViewModelProvider(this, EditNutritionCustomerTrainerViewModelFactory(customerId, ManageCustomerTrainerUseCaseImpl(UserRepositoryImpl()))).get(EditNutritionCustomerTrainerViewModel::class.java)}
    private lateinit var binding: FragmentNutritionCustomerTrainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_nutrition_customer_trainer,
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
        setupUI()
    }

    private fun setupUI(){
        val darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        if(darkMode){
            Glide.with(this).load(R.drawable.proteins_dark).into(protein_image)
            Glide.with(this).load(R.drawable.carbohydrates_dark).into(carbohydrate_image)
            Glide.with(this).load(R.drawable.fats_dark).into(fats_image)
        }else{
            Glide.with(this).load(R.drawable.proteins).into(protein_image)
            Glide.with(this).load(R.drawable.carbohydrates).into(carbohydrate_image)
            Glide.with(this).load(R.drawable.fats).into(fats_image)
        }
        radioGrp_gainlose.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == radioHipercaloric.id){
                viewModel.selectedFormulaType.value = "Hipercaloric"
            }else if(checkedId == radioHypocaloric.id){
                viewModel.selectedFormulaType.value = "Hypocaloric"
            }
        }
    }

    private fun observeData(){
        viewModel.getCustomer.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it is Resource.Success){
                    viewModel.loadData(it)
                    if(it.data.formulaType == "Hypocaloric"){
                        radioHypocaloric.isChecked = true
                    }else{
                        radioHipercaloric.isChecked = true
                    }
                }
            }
        })

        viewModel.proteins.observe(viewLifecycleOwner, Observer {
            protein_column.text = viewModel.proteins.value + " g."
        })
        viewModel.carbohydrates.observe(viewLifecycleOwner, Observer {
            carbohydrate_column.text = viewModel.carbohydrates.value + " g."
        })
        viewModel.fats.observe(viewLifecycleOwner, Observer {
            fat_column.text = viewModel.fats.value + " g."
        })

        viewModel.selectedFormula.observe(viewLifecycleOwner, Observer {
            viewModel.changeCalories()
        })

        viewModel.selectedFormulaType.observe(viewLifecycleOwner, Observer {
            viewModel.changeCalories()
        })

        viewModel.loadedData.observe(viewLifecycleOwner, Observer {result ->
            when(result){
                true ->{
                    setupSpinnerAdapter()
                }
            }

        })

        viewModel.calories.observe(viewLifecycleOwner, Observer {
            daily_calories_customer_trainer.text = viewModel.calories.value
        })

        viewModel.customerEdited.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //TODO: hideProgress()
                    findNavController().navigate(NutritionCustomerTrainerFragmentDirections.actionNutritionCustomerTrainerFragmentToDisplayCustomer(customerId, viewModel.name.value!!))
                }
                false -> {
                    //TODO: hideProgress()
                    Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        })

    }

    private fun setupSpinnerAdapter() {
        viewModel.formulas.observe(viewLifecycleOwner, Observer {
            it?.let {
                val spinner: Spinner = nutrition_trainer_customer_formula_spinner
                ArrayAdapter(
                    this.context!!,
                    android.R.layout.simple_spinner_item,
                    it
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    spinner.setSelection(adapter.getPosition(viewModel.selectedFormula.value))
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                                viewModel.selectFormula(it[position])
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
            }
        })
        /*
        viewModel.formulaTypes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val spinner: Spinner = nutrition_trainer_customer_formula_type_spinner
                ArrayAdapter(
                    this.context!!,
                    android.R.layout.simple_spinner_item,
                    it
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    spinner.setSelection(adapter.getPosition(viewModel.selectedFormulaType.value))
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                           viewModel.selectFormulaType(it[position])

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
            }
        })

         */
    }
}
