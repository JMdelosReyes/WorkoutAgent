package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Day
import kotlinx.android.synthetic.main.item_row_routine_day.view.*


class DayListAdapter(private val context: Context, private val clickListener: (day: Day) -> Unit) :
    RecyclerView.Adapter<DayListAdapter.DayListViewHolder>() {

    private var dataList = mutableListOf<Day>()
    private val viewPool = RecyclerView.RecycledViewPool()
    fun setListData(data: MutableList<Day>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_row_routine_day, parent, false)
        return DayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }


    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: DayListViewHolder, position: Int) {
        val day: Day = dataList[position]
        holder.bindView(day)

        val childLayoutManager =
            LinearLayoutManager(holder.recyclerView.context, LinearLayout.VERTICAL, false)

        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = ActivityListChildAdapter(day.activities)
            setRecycledViewPool(viewPool)
        }
    }


    inner class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.recyclerViewRoutineDayActivity
        lateinit var day: Day
        fun bindView(day: Day) {
            this.day = day
            itemView.row_routine_day_name.text = day.name
            itemView.edit_activity_button.setOnClickListener {
                clickListener(day)
            }
        }
    }
}