package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Day
import kotlinx.android.synthetic.main.item_row_routine_day.view.*


class DayListAdapter(private val context: Context) : RecyclerView.Adapter<DayListAdapter.DayListViewHolder>() {

    private var dataList = mutableListOf<Day>()
    fun setListData(data: MutableList<Day>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_routine_day, parent, false)
        return DayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: DayListViewHolder, position: Int) {
        val day: Day = dataList[position]
        holder.bindView(day)
    }

    inner class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(day: Day) {

            itemView.row_routine_day_name.text = day.name


        }
    }
}