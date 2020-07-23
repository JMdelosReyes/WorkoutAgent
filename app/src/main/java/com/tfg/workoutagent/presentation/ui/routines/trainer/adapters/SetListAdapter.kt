package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.ActivitySet
import kotlinx.android.synthetic.main.item_row_set.view.*

class SetListAdapter(
    private val context: Context,
    private val removeSetListener: (position: Int) -> Unit,
    private val updateSetListener: (repetitions: Int?, weight: Double?, position: Int) -> String
) :
    RecyclerView.Adapter<SetListAdapter.SetListViewHolder>() {

    private var dataList = mutableListOf<ActivitySet>()
    fun setListData(data: MutableList<ActivitySet>) {
        dataList = data
    }

    fun addElement(element: ActivitySet) {
        this.dataList.add(element)
        this.notifyItemInserted(this.dataList.size)
    }

    fun removeElement(position: Int) {
        this.dataList.removeAt(position)
        this.notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SetListAdapter.SetListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_set, parent, false)
        return SetListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: SetListAdapter.SetListViewHolder, position: Int) {
        holder.bindView(dataList[position])
    }

    inner class SetListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var activitySet: ActivitySet

        fun bindView(activitySet: ActivitySet) {
            this.activitySet = activitySet
            itemView.repetitions_activity_input.setText(this.activitySet.repetitions.toString())
            itemView.weights_activity_input.setText(this.activitySet.weight.toString())

            // Listener delete set
            itemView.remove_set_button.setOnClickListener {
                removeSetListener(adapterPosition)
                removeElement(adapterPosition)
            }

            // Listeners update
            itemView.repetitions_activity_input.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val repetitions =
                        itemView.repetitions_activity_input.text.toString().toIntOrNull()
                    val weight = itemView.weights_activity_input.text.toString().toDoubleOrNull()
                    val activitySetError = updateSetListener(repetitions, weight, adapterPosition)

                    if (activitySetError != "") {
                        itemView.activity_set_error_message.text = activitySetError
                        itemView.activity_set_error_message.visibility = View.VISIBLE
                    } else {
                        itemView.activity_set_error_message.text = activitySetError
                        itemView.activity_set_error_message.visibility = View.GONE
                    }
                }
            }

            itemView.weights_activity_input.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val repetitions =
                        itemView.repetitions_activity_input.text.toString().toIntOrNull()
                    val weight = itemView.weights_activity_input.text.toString().toDoubleOrNull()
                    val activitySetError = updateSetListener(repetitions, weight, adapterPosition)

                    if (activitySetError != "") {
                        itemView.activity_set_error_message.text = activitySetError
                        itemView.activity_set_error_message.visibility = View.VISIBLE
                    } else {
                        itemView.activity_set_error_message.text = activitySetError
                        itemView.activity_set_error_message.visibility = View.GONE
                    }
                }
            }
        }
    }
}