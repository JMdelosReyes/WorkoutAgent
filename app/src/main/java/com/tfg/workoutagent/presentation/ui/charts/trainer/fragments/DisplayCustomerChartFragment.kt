package com.tfg.workoutagent.presentation.ui.charts.trainer.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.MarkerImage
import com.github.mikephil.charting.data.*
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseFragment
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.domain.userUseCases.ManageCustomerTrainerUseCaseImpl
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.presentation.ui.charts.trainer.viewmodels.DisplayCustomerChartsViewModel
import com.tfg.workoutagent.presentation.ui.charts.trainer.viewmodels.DisplayCustomerChartsViewModelFactory
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModelFactory
import com.tfg.workoutagent.presentation.ui.users.admin.fragments.DisplayCustomerAdminFragmentArgs
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.getAge
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.fragment_charts.*
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class DisplayCustomerChartFragment : BaseFragment() {

    private val customerId by lazy { DisplayCustomerChartFragmentArgs.fromBundle(arguments!!).customerId}
    private val viewModel by lazy { ViewModelProvider(this, DisplayCustomerChartsViewModelFactory(customerId, ManageCustomerTrainerUseCaseImpl(UserRepositoryImpl()))).get(DisplayCustomerChartsViewModel::class.java)}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_charts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(customer: Customer) {

        val entries = ArrayList<Entry>()
        var i = 1f
        for(weight in customer.weights){
            entries.add(Entry(i, weight.weight.toFloat()))
            i++
        }

        val vl = LineDataSet(entries, "Weights")

        if(getDarkMode()){
            vl.valueTextColor = Color.WHITE
            lineChart.legend.textColor = Color.WHITE
        }else{
            vl.valueTextColor = Color.BLACK
            lineChart.legend.textColor = Color.BLACK
        }
        vl.setDrawValues(true)
        vl.setDrawFilled(true)
        //Color del valor de cada punto

        vl.lineWidth = 3f

        lineChart.xAxis.labelRotationAngle = 0f

        //Poner o quitar cuadricula
        lineChart.axisLeft.setDrawGridLines(false);
        lineChart.xAxis.setDrawGridLines(false);

        //Configurar la altura mínima del gráfico
        lineChart.minimumHeight = 400
        lineChart.extraBottomOffset = -50f
        //Configurar los colores del texto

        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE

        lineChart.data = LineData(vl)

        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = i+0.1f

        //Prueba
        lineChart.axisLeft.isEnabled = false
        lineChart.xAxis.isEnabled = false


        lineChart.setTouchEnabled(false)
        lineChart.setPinchZoom(false)


        lineChart.description.text = ""
        lineChart.setNoDataText("Something went wrong")

        lineChart.animateX(500, Easing.EaseInExpo)

        val age = getAge(parseDateToFriendlyDate(customer.birthday)!!)
        val currentWeight = customer.weights[customer.weights.size-1].weight
        val idealWeight = customer.height - 100 + ((age/10) * 0.9).roundToInt()
        ideal_weight.text = "The perfect weight is $idealWeight kg"
        val height = customer.height.toDouble()/100
        val imc =  String.format("%.2f", (currentWeight/(height.pow(2))))
        current_imc.text = "The BMI is $imc"


    }

    private fun observeData() {
        viewModel.getProfileCustomer.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Log.i("Customer Prueba","Funciona")
                    setupUI(it.data)
                }
                is Resource.Failure -> {
                }
            }
        })
    }
}
