package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.vo.createAlertDialog
import kotlinx.android.synthetic.main.item_row_routine_activity_historic.view.*

class ListHistoricActivitiesCustomerAdapter(private val context: Context) : RecyclerView.Adapter<ListHistoricActivitiesCustomerAdapter.ListHistoricActivitiesCustomerViewHolder>() {

    private var dataList = mutableListOf<RoutineActivity>()
    fun setListData(data: MutableList<RoutineActivity>) {
        dataList = data
    }

    override fun onBindViewHolder(holder: ListHistoricActivitiesCustomerViewHolder, position: Int) {
        val routineActivity = dataList[position]
        holder.bindView(routineActivity)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListHistoricActivitiesCustomerViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_row_routine_activity_historic, parent, false)
        return ListHistoricActivitiesCustomerViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ListHistoricActivitiesCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(routineActivity: RoutineActivity) {
            //SETEO DE SETS ESTIMADAS
            itemView.name_activity_estimated_historic.text = routineActivity.name + " (Estimated)"
            routineActivity.repetitions.forEachIndexed { index, valor ->
                val repetitionView = TextView(context).apply {
                    val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    this.layoutParams = params
                    this.typeface = Typeface.DEFAULT_BOLD
                    this.text = valor.toString()
                }
                val weightView = TextView(context).apply {
                    val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    this.layoutParams = params
                    this.typeface = Typeface.DEFAULT_BOLD
                    var weight = routineActivity.weightsPerRepetition[index].toString()
                    if (weight.split(".")[1] == "0") {
                        weight = weight.split(".")[0]
                    }
                    this.text = weight
                }
                val relativeLayout = RelativeLayout(context).apply {
                    val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    params.marginStart = 20
                    this.layoutParams = params

                    val relativeParamsRepetition: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        )
                    relativeParamsRepetition.apply {
                        this.addRule(RelativeLayout.CENTER_HORIZONTAL)
                    }

                    val relativeParamsWeight: RelativeLayout.LayoutParams =
                        RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        )
                    relativeParamsWeight.apply {
                        this.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        this.addRule(RelativeLayout.CENTER_HORIZONTAL)
                    }

                    this.addView(repetitionView, relativeParamsRepetition)
                    this.addView(weightView, relativeParamsWeight)
                }
                itemView.ll_repetitions_weights_estimated_historic.addView(relativeLayout)
            }
            //SETEO DE SETS REALES
            if(!routineActivity.completed){
                itemView.cl_name_repetitions_weights_real_historic.visibility = View.GONE
            }else{
                itemView.name_activity_real_historic.text = routineActivity.name + " (Finished)"
                routineActivity.repetitionsCustomer.forEachIndexed { index, valor ->
                    val repetitionView = TextView(context).apply {
                        val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        this.layoutParams = params
                        this.typeface = Typeface.DEFAULT_BOLD
                        this.text = valor.toString()
                    }
                    val weightView = TextView(context).apply {
                        val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        this.layoutParams = params
                        this.typeface = Typeface.DEFAULT_BOLD
                        var weight = routineActivity.weightsPerRepetitionCustomer[index].toString()
                        if (weight.split(".")[1] == "0") {
                            weight = weight.split(".")[0]
                        }
                        this.text = weight
                    }
                    val relativeLayout = RelativeLayout(context).apply {
                        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        params.marginStart = 20
                        this.layoutParams = params

                        val relativeParamsRepetition: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        relativeParamsRepetition.apply {
                            this.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        }

                        val relativeParamsWeight: RelativeLayout.LayoutParams =
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        relativeParamsWeight.apply {
                            this.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            this.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        }

                        this.addView(repetitionView, relativeParamsRepetition)
                        this.addView(weightView, relativeParamsWeight)
                    }
                    itemView.ll_repetitions_weights_real_historic.addView(relativeLayout)
                }
            }
            itemView.description_activity_historic.setOnClickListener { createAlertDialog(context, "Exercise description", routineActivity.exercise.description) }
            if(routineActivity.note != "" && routineActivity.note != "DEFAULT_NOTE"){
                itemView.note_activity_historic.visibility = View.VISIBLE
                itemView.note_activity_historic.setOnClickListener { createAlertDialog(context, "Activity note", routineActivity.note) }
            }
            Glide.with(context).load(routineActivity.exercise.photos[0])
                .into(itemView.civ_activity_historic)
        }
    }
}