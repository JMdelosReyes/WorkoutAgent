package com.tfg.workoutagent.presentation.ui.goals.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Goal
import kotlinx.android.synthetic.main.item_row_goal.view.*
import java.text.SimpleDateFormat
import java.util.*

class GoalsCustomerTrainerListAdapter (private val context: Context, private val darkMode : Boolean):
    RecyclerView.Adapter<GoalsCustomerTrainerListAdapter.GoalsCustomerTrainerListViewHolder> (){
    var dataGoalsList = mutableListOf<Goal>()
    fun setListData(data: MutableList<Goal>){
        dataGoalsList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsCustomerTrainerListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_goal, parent, false)
        return GoalsCustomerTrainerListViewHolder(view)
    }

    override fun getItemCount(): Int = if (dataGoalsList.size > 0) dataGoalsList.size else 0

    override fun onBindViewHolder(holder: GoalsCustomerTrainerListViewHolder, position: Int) {
        val goal : Goal = dataGoalsList[position]
        holder.itemView.setOnClickListener {
           if(it.row_goal_description.isVisible){
               it.row_goal_description.visibility = View.GONE
           }else{
               it.row_goal_description.visibility = View.VISIBLE
           }
        }
        holder.bindView(goal)
    }

    inner class GoalsCustomerTrainerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(goal: Goal){
            if(darkMode) {
                itemView.ll_all_goal.setBackgroundResource(R.drawable.goal_item_border_dark)
            }else{
                itemView.ll_all_goal.setBackgroundResource(R.drawable.goal_item_border)
            }
            if(goal.isAchieved){
                Glide.with(context).load(R.drawable.ic_trophy_accomplished_48dp).into(itemView.circleImageViewGoal)
            }else{
                Glide.with(context).load(R.drawable.ic_trophy_not_accomplished_48dp).into(itemView.circleImageViewGoal)
            }
            itemView.row_goal_aim.text = goal.aim
            itemView.row_goal_description.text = goal.description
            val currentTime = Calendar.getInstance().time
            if(goal.startDate.before(currentTime) && goal.endDate.after(currentTime)){
                val days_left = ((((goal.endDate.time - currentTime.time)/1000)/60)/60)/24
                if(days_left.toInt() > 0){
                    itemView.row_goal_deadline.text = "$days_left days left to go"
                }else if (days_left.toInt() == 0){
                    itemView.row_goal_deadline.text = "Today is the last day"
                }
            }else if(goal.endDate.before(currentTime)){
                val pattern = "dd-MM-yyyy"
                val simpleDateFormat = SimpleDateFormat(pattern)
                val date: String = simpleDateFormat.format(goal.endDate)
                itemView.row_goal_deadline.text = "This goal ended at $date"
            }else if(goal.startDate.after(currentTime)){
                val pattern = "dd-MM-yyyy"
                val simpleDateFormat = SimpleDateFormat(pattern)
                val date: String = simpleDateFormat.format(goal.startDate)
                itemView.row_goal_deadline.text = "This goal will start at $date"
            }
        }
    }
}