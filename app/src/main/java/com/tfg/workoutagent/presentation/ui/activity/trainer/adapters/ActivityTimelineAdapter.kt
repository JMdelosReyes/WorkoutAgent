package com.tfg.workoutagent.presentation.ui.activity.trainer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.TimelineActivity
import com.tfg.workoutagent.presentation.ui.activity.trainer.fragments.ActivityTrainerFragmentDirections
import com.tfg.workoutagent.presentation.ui.users.trainer.fragments.UserTrainerFragmentDirections
import kotlinx.android.synthetic.main.item_row_activity_timeline.view.*
import kotlin.math.truncate

class ActivityTimelineAdapter(private val context: Context) :
    RecyclerView.Adapter<ActivityTimelineAdapter.ActivityTimelineViewHolder>() {

    private var dataList = mutableListOf<TimelineActivity>()
    fun setListData(data: MutableList<TimelineActivity>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTimelineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_activity_timeline, parent, false)
        return ActivityTimelineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ActivityTimelineViewHolder, position: Int) {
        val timelineActivity: TimelineActivity = dataList[position]
        holder.bindView(timelineActivity)
    }

    inner class ActivityTimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(timelineActivity: TimelineActivity) {
            if(timelineActivity.customerPhoto == "" || timelineActivity.customerPhoto == "DEFAULT_IMAGE" || timelineActivity.customerPhoto == "DEFAULT_PHOTO"){
                Glide.with(context).load(R.drawable.ic_person_black_60dp)
                    .into(itemView.circleImageView_activity_timeline)
            }else{
                Glide.with(context).load(timelineActivity.customerPhoto)
                    .into(itemView.circleImageView_activity_timeline)
            }
            itemView.row_customer_name_timeline.text = timelineActivity.customerName


            val minutesAgo = System.currentTimeMillis()/60000 - timelineActivity.finishDate.time/60000
            if(minutesAgo < 1){
                itemView.row_finishDate_message.text = "Completed his day a few seconds ago"
            }else if (minutesAgo < 60){
                itemView.row_finishDate_message.text = "Completed his day $minutesAgo minutes ago"
            }else if(minutesAgo < 60*24){
                var hours = minutesAgo/60
                hours = truncate(hours.toDouble()).toLong()
                itemView.row_finishDate_message.text = "Completed his day $hours hours ago"
            }else if(minutesAgo < 60*24*7){
                var days = minutesAgo/(60*24)
                days = truncate(days.toDouble()).toLong()
                if(days.toInt() == 1){
                    itemView.row_finishDate_message.text = "Completed his day yesterday"
                }else{
                    itemView.row_finishDate_message.text = "Completed his day $days days ago"
                }

            }else{
                itemView.row_finishDate_message.text = "Completed his day a long time ago"
            }
            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    ActivityTrainerFragmentDirections.actionNavigationActivityTrainerToDisplayCustomer(customerId = timelineActivity.customerId, customerName = timelineActivity.customerName)
                )
            }

        }
    }
}