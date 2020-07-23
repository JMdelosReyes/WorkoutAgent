package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.RoutineActivity
import kotlinx.android.synthetic.main.item_row_display_routine_day_activity_child.view.*


class DisplayActivityListChildAdapter(
    private val context: Context,
    private val children: MutableList<RoutineActivity>
) :
    RecyclerView.Adapter<DisplayActivityListChildAdapter.ActivityListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_display_routine_day_activity_child, parent, false)
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
            Glide.with(context).load(routineActivity.exercise.photos[0])
                .into(itemView.row_routine_day_activity_photo)
        }
    }
}