package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DisplayActivityListChildAdapter
import kotlinx.android.synthetic.main.fragment_my_routine_customer.view.*
import kotlinx.android.synthetic.main.item_row_routine_day_customer.view.*

class MyRoutineDaysListAdapter(private val context: Context, private val darkMode : Boolean) : RecyclerView.Adapter<MyRoutineDaysListAdapter.MyRoutineDayListViewHolder>() {

    var dataDayList = mutableListOf<Day>()
    private val viewPool = RecyclerView.RecycledViewPool()
    fun setDataList(data: MutableList<Day>){
        dataDayList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRoutineDayListViewHolder {
        val view =
        LayoutInflater.from(context)
            .inflate(R.layout.item_row_routine_day_customer, parent, false)
        return MyRoutineDayListViewHolder(view)
    }


    override fun getItemCount(): Int = dataDayList.size

    override fun onBindViewHolder(holder: MyRoutineDayListViewHolder, position: Int) {
       val day : Day = dataDayList[position]
        holder.bindView(day)

        val childLayoutManager = GridLayoutManager(holder.recyclerView.context, 2)
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = DisplayActivityListChildAdapter(context, day.activities)
            setRecycledViewPool(viewPool)
        }
    }

    inner class MyRoutineDayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recyclerView: RecyclerView = itemView.rcv_activities
        lateinit var day: Day
        fun bindView(day: Day) {
            this.day = day
            itemView.row_routine_day_name_customer.text = day.name
            this.recyclerView.visibility = View.GONE
            itemView.setOnClickListener {
                if (this.recyclerView.visibility == View.GONE) {
                    this.recyclerView.visibility = View.VISIBLE
                } else {
                    this.recyclerView.visibility = View.GONE
                }
            }
        }
    }
}