package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseAdapterInterface
import com.tfg.workoutagent.models.ActivitySet
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.presentation.ui.routines.customer.fragments.TodayActivitiesCustomerFragment
import kotlinx.android.synthetic.main.item_row_rotuine_activity_today.view.*

class TodayActivitiesCustomerListAdapter(
    private val context: Context,
    private val completedClickListener: (position: Int) -> Unit,
    private val actionListeners: TodayActivitiesCustomerFragment.ActionListeners
) : RecyclerView.Adapter<TodayActivitiesCustomerListAdapter.TodayActivitiesCustomerListViewHolder>(),
    BaseAdapterInterface {

    var dataRoutineActivityList = mutableListOf<RoutineActivity>()
    private val viewPool = RecyclerView.RecycledViewPool()

    fun setDataList(data: MutableList<RoutineActivity>) {
        dataRoutineActivityList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodayActivitiesCustomerListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_rotuine_activity_today, parent, false)
        return TodayActivitiesCustomerListViewHolder(view)
    }

    override fun getItemCount(): Int = dataRoutineActivityList.size


    override fun onBindViewHolder(holder: TodayActivitiesCustomerListViewHolder, position: Int) {
        val activity: RoutineActivity = dataRoutineActivityList[position]
        if (!activity.completed) {
            holder.itemView.setOnClickListener {
                if (it.ll_invisible_content.isVisible) {
                    it.ll_invisible_content.visibility = View.GONE
                } else {
                    it.ll_invisible_content.visibility = View.VISIBLE
                }
            }


        } else if (activity.completed && activity.repetitionsCustomer.size == 0) {
            holder.itemView.card_today.setBackgroundResource(R.drawable.item_border_no_sets)
        } else {
            holder.itemView.card_today.setBackgroundResource(R.drawable.item_border_set_completed)
        }
        holder.bindView(activity)
    }

    inner class TodayActivitiesCustomerListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var routineActivity: RoutineActivity
        fun bindView(routineActivity: RoutineActivity) {
            this.routineActivity = routineActivity

            val sets = mutableListOf<ActivitySet>()
            for (i in 0 until (routineActivity.repetitionsCustomer.size)) {
                sets.add(
                    ActivitySet(
                        routineActivity.repetitionsCustomer[i],
                        routineActivity.weightsPerRepetitionCustomer[i]
                    )
                )
            }

            val setsAdapter = EditWeightsRepetitionsRoutineActivityCustomerListAdapter(
                context,
                adapterPosition,
                actionListeners
            )

            itemView.rcv_sets_today_activity.layoutManager = LinearLayoutManager(context)
            itemView.rcv_sets_today_activity.adapter = setsAdapter
            setsAdapter.setListData(sets)

            itemView.ll_repetitions_weights.removeAllViews()
            if (this.routineActivity.completed) {
                itemView.name_activity_today.text = routineActivity.name + " (Finished)"
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
                    itemView.ll_repetitions_weights.addView(relativeLayout)
                }
            } else {
                itemView.name_activity_today.text = routineActivity.name + " (Estimated)"
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
                    itemView.ll_repetitions_weights.addView(relativeLayout)
                }
            }

            itemView.description_activity_today.text = routineActivity.exercise.description
            itemView.note_activity_today.text = routineActivity.note
            Glide.with(context).load(routineActivity.exercise.photos[0])
                .into(itemView.civ_activity_today)
            itemView.button_finish_activity.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Finish this exercise")
                builder.setMessage("Once you finish this exercise, you cannot modify it. Are you sure?")

                builder.setPositiveButton("Yes") { dialog, _ ->
                    completedClickListener(adapterPosition)
                    itemView.card_today.setBackgroundResource(R.drawable.item_border_set_completed)
                    itemView.ll_invisible_content.visibility = View.GONE
                    dialog.dismiss()
                    notifyItemChanged(adapterPosition)
                }
                builder.setNeutralButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.create()
                builder.show()
            }

        }
    }
}