package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.RoutineActivity
import kotlinx.android.synthetic.main.item_row_routine_day_activity.view.*
import kotlin.math.roundToInt

class ActivityListAdapter(
    private val context: Context,
    private val clickListener: (routineActivity: RoutineActivity) -> Unit
) :
    RecyclerView.Adapter<ActivityListAdapter.ActivityListViewHolder>() {

    private var dataList = mutableListOf<RoutineActivity>()
    fun setListData(data: MutableList<RoutineActivity>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_routine_day_activity, parent, false)
        return ActivityListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ActivityListViewHolder, position: Int) {
        val activity: RoutineActivity = dataList[position]
        holder.bindView(activity)
    }

    inner class ActivityListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var activity: RoutineActivity
        fun bindView(activity: RoutineActivity) {
            Glide.with(context).load(activity.exercise.photos[0])
                .into(itemView.row_routine_day_activity_photo)
            this.activity = activity
            itemView.edit_day_activity_button.setOnClickListener {
                clickListener(activity)
            }
            itemView.row_routine_day_activity_name.text = activity.name
            itemView.row_routine_day_activity_repetitions.text =
                activity.repetitions.joinToString(" ")

            itemView.row_routine_day_activity_weights.text =
                doubleListToString(activity.weightsPerRepetition)
        }
    }

    private fun doubleListToString(doubleList: MutableList<Double>): String {
        var listString = ""
        var i = 0
        for (item in doubleList) {
            if (i == doubleList.size - 1) {
                listString = listString + item.roundToInt()
            } else {
                listString = listString + item.roundToInt() + " "
            }
            i++
        }

        return listString
    }
}