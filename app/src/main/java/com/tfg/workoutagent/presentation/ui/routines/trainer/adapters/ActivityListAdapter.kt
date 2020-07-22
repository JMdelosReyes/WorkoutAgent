package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

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
import kotlinx.android.synthetic.main.item_row_routine_day_activity.view.*

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

            activity.repetitions.forEachIndexed { index, valor ->
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
                    var weight = activity.weightsPerRepetition[index].toString()
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
                itemView.relative_content.addView(relativeLayout)
            }
        }
    }
}