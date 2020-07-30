package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.ActivitySet
import com.tfg.workoutagent.presentation.ui.routines.customer.fragments.TodayActivitiesCustomerFragment
import kotlinx.android.synthetic.main.item_row_set_edit.view.*

class EditWeightsRepetitionsRoutineActivityCustomerListAdapter(
    private val context: Context,
    private val listActivitySets: MutableList<ActivitySet>,
    private val actionListeners: TodayActivitiesCustomerFragment.ActionListeners

) : RecyclerView.Adapter<EditWeightsRepetitionsRoutineActivityCustomerListAdapter.EditWeightsRepetitionsRoutineActivityCustomerViewHolder>() {


    private var dataList = listActivitySets
    fun setListData(data: MutableList<ActivitySet>) {
        dataList = data
    }
    override fun getItemCount(): Int = listActivitySets.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditWeightsRepetitionsRoutineActivityCustomerListAdapter.EditWeightsRepetitionsRoutineActivityCustomerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_set_edit, parent, false)
        return EditWeightsRepetitionsRoutineActivityCustomerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EditWeightsRepetitionsRoutineActivityCustomerListAdapter.EditWeightsRepetitionsRoutineActivityCustomerViewHolder,
        position: Int
    ) {
        val rowSet : ActivitySet = listActivitySets[position]
        holder.bindView(rowSet)
    }

    fun finishedActivitySetListener(position: Int){
        this.notifyItemChanged(position)
    }

    fun removeActivitySetListener(position:Int){
        this.dataList.removeAt(position)
        this.notifyItemRemoved(position)
    }

    inner class EditWeightsRepetitionsRoutineActivityCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(activitySet: ActivitySet){
            itemView.repetitions_activity_input_edit.setText(activitySet.repetitions.toString())
            itemView.weights_activity_input_edit.setText(activitySet.weight.toString())
            itemView.delete_set_button_edit.setOnClickListener{
                actionListeners.deleteClickListener(adapterPosition)
                removeActivitySetListener(adapterPosition)
            }
            itemView.finished_set_button_edit.setOnFocusChangeListener { v, hasFocus ->
                if(!hasFocus){
                    val errorValidation = actionListeners.finishedClickListener(adapterPosition,
                        itemView.repetitions_activity_input_edit.text.toString().toIntOrNull(),
                        itemView.weights_activity_input_edit.toString().toDoubleOrNull()
                    )
                    if(errorValidation != "") {
                        itemView.activity_set_error_message_edit.text = errorValidation
                        itemView.activity_set_error_message_edit.visibility = View.VISIBLE
                    }else{
                        itemView.activity_set_error_message_edit.text = errorValidation
                        itemView.activity_set_error_message_edit.visibility = View.GONE
                        finishedActivitySetListener(adapterPosition)
                    }
                }
            }
        }
    }
}