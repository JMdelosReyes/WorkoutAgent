package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.RoutineActivity
import kotlinx.android.synthetic.main.item_row_routine_day_activity.view.*
import kotlin.math.roundToInt


class ActivityListChildAdapter(private val children: MutableList<RoutineActivity>) :
    RecyclerView.Adapter<ActivityListChildAdapter.ActivityListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_routine_day_activity_child, parent, false)
        return ActivityListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (children.size > 0) {
            children.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ActivityListViewHolder, position: Int) {
        val routineActivity: RoutineActivity = children[position]
        holder.bindView(routineActivity)
    }

    inner class ActivityListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(routineActivity: RoutineActivity) {
            itemView.row_routine_day_activity_name.text = routineActivity.name
            itemView.row_routine_day_activity_repetitions.text =
                "Repetitions: ${routineActivity.repetitions.joinToString(",")}"
            itemView.row_routine_day_activity_weights.text =
                "Weights: " + doubleListToString(routineActivity.weightsPerRepetition)
        }
    }

    private fun doubleListToString(doubleList: MutableList<Double>): String {
        var listString = ""
        for ((i, item) in doubleList.withIndex()) {
            if (i == doubleList.size - 1) {
                listString += item.roundToInt()
            } else {
                listString = listString + item.roundToInt() + ","
            }
        }

        return listString
    }
}