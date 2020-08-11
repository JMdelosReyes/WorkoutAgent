package com.tfg.workoutagent.presentation.ui.charts.customer.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.MarkerImage
import com.github.mikephil.charting.data.*
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.domain.profileUseCases.DisplayProfileUserUseCaseImpl
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModel
import com.tfg.workoutagent.presentation.ui.profile.customer.viewModels.ProfileCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import com.tfg.workoutagent.vo.utils.getAge
import com.tfg.workoutagent.vo.utils.parseDateToFriendlyDate
import kotlinx.android.synthetic.main.fragment_charts.*
import kotlin.math.roundToInt

class DisplayChartFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(
            this, ProfileCustomerViewModelFactory(
                DisplayProfileUserUseCaseImpl(
                    UserRepositoryImpl()
                )
            )
        ).get(ProfileCustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun setupUI(customer: Customer) {

        val entries = ArrayList<Entry>()
        var i = 1f
        for(weight in customer.weights){
            entries.add(Entry(i, weight.weight.toFloat()))
            i++
        }

        val vl = LineDataSet(entries, "Weights")

        vl.setDrawValues(true)
        vl.setDrawFilled(true)
        //Color del valor de cada punto
        vl.valueTextColor = Color.WHITE
        vl.lineWidth = 3f
        vl.fillColor = R.color.colorPrimary
        vl.fillAlpha = R.color.colorPrimaryDark

        lineChart.xAxis.labelRotationAngle = 0f

        //Poner o quitar cuadricula
        lineChart.axisLeft.setDrawGridLines(false);
        lineChart.xAxis.setDrawGridLines(false);

        //Configurar la altura mínima del gráfico
        lineChart.minimumHeight = 400
        lineChart.extraBottomOffset = -50f
        //Configurar los colores del texto
        lineChart.legend.textColor = Color.WHITE
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE

        lineChart.data = LineData(vl)

        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = i+0.1f

        /*//Prueba
        lineChart.axisLeft.isEnabled = false
        lineChart.xAxis.isEnabled = false*/


        lineChart.setTouchEnabled(false)
        lineChart.setPinchZoom(false)


        lineChart.description.text = ""
        lineChart.setNoDataText("Something went wrong")

        lineChart.animateX(3000, Easing.EaseInExpo)

        val age = getAge(parseDateToFriendlyDate(customer.birthday)!!)
        val currentWeight = customer.weights[customer.weights.size-1].weight
        val idealWeight = customer.height - 100 + ((age/10) * 0.9).roundToInt()
        ideal_weight.text = "Your perfect weight is $idealWeight kg"
        val imc = currentWeight/((customer.height/100)*(customer.height/100))


        //Grafica 2
        val entries2 = ArrayList<BarEntry>()

        entries2.add(BarEntry(1f, floatArrayOf(18.5f,25f,29.9f)))

        val vl2 = BarDataSet(entries2, "")
        vl2.stackLabels = arrayOf("Bajo", "Normal", "Ha aumentado", "Alto")
        vl2.setDrawValues(true)
        vl2.valueTextSize = 8f
        //Color del valor de cada punto
        vl2.valueTextColor = Color.WHITE
        vl2.setColors(Color.rgb(23, 161, 234), Color.rgb(12, 197, 96),Color.rgb(250, 218, 76 ), Color.rgb(240, 153, 27 ))

        stackedBarChar.xAxis.labelRotationAngle = 0f

        //Poner o quitar cuadricula
        stackedBarChar.axisLeft.setDrawGridLines(false);
        stackedBarChar.xAxis.setDrawGridLines(false);

        //Configurar la altura mínima del gráfico
        stackedBarChar.minimumHeight = 200
        stackedBarChar.extraBottomOffset = 15f
        //Configurar los colores del texto
        stackedBarChar.legend.textColor = Color.WHITE
        stackedBarChar.xAxis.textColor = Color.WHITE
        stackedBarChar.axisLeft.textColor = Color.WHITE

        stackedBarChar.data = BarData(vl2)

        stackedBarChar.axisRight.isEnabled = false
        stackedBarChar.xAxis.axisMaximum = 5+0.1f


        stackedBarChar.axisLeft.isEnabled = false
        stackedBarChar.xAxis.isEnabled = false


        stackedBarChar.setTouchEnabled(false)
        stackedBarChar.setPinchZoom(false)


        stackedBarChar.description.text = ""
        stackedBarChar.setNoDataText("Something went wrong")

        //Part10
        stackedBarChar.animateX(3000, Easing.EaseInExpo)


        /*val entries2 = ArrayList<BarEntry>()

        entries2.add(BarEntry(1f, floatArrayOf(45f,55f,68f,78f,86f)))

        val vl2 = BarDataSet(entries2, "cosas")

        vl2.setDrawValues(true)
        vl2.valueTextSize = 12f
        //Color del valor de cada punto
        vl2.valueTextColor = Color.WHITE
        vl2.setColors(Color.rgb(210, 34, 231), Color.YELLOW,Color.BLUE, Color.GREEN)

        stackedBarChar.xAxis.labelRotationAngle = 0f

        //Poner o quitar cuadricula
        stackedBarChar.axisLeft.setDrawGridLines(false);
        stackedBarChar.xAxis.setDrawGridLines(false);

        //Configurar la altura mínima del gráfico
        stackedBarChar.minimumHeight = 200
        //stackedBarChar.extraBottomOffset = -50f
        //Configurar los colores del texto
        stackedBarChar.legend.textColor = Color.WHITE
        stackedBarChar.xAxis.textColor = Color.WHITE
        stackedBarChar.axisLeft.textColor = Color.WHITE

        stackedBarChar.data = BarData(vl2)

        stackedBarChar.axisRight.isEnabled = false
        stackedBarChar.xAxis.axisMaximum = 5+0.1f

        //Prueba
        stackedBarChar.axisLeft.isEnabled = false
        stackedBarChar.xAxis.isEnabled = false


        stackedBarChar.setTouchEnabled(false)
        stackedBarChar.setPinchZoom(false)


        stackedBarChar.description.text = ""
        stackedBarChar.setNoDataText("Something went wrong")

        //Part10
        stackedBarChar.animateX(1800, Easing.EaseInExpo)*/



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
