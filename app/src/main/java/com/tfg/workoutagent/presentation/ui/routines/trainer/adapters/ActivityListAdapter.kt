package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.RoutineActivity
import kotlinx.android.synthetic.main.item_row_routine_day_activity.view.*


class ActivityListAdapter(private val children: MutableList<RoutineActivity>) : RecyclerView.Adapter<ActivityListAdapter.ActivityListViewHolder>() {

    /*private var dataList = mutableListOf<RoutineActivity>()
    fun setListData(data: MutableList<RoutineActivity>) {
        dataList = data
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_routine_day_activity, parent, false)
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
        fun bindView(routineActivity: RoutineActivity) {

            itemView.row_routine_day_activity_name.text = routineActivity.name


        }
    }
}