package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.DisplayActivityListChildAdapter
import kotlinx.android.synthetic.main.item_row_routine_day_customer.view.*
import java.util.*

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
            itemView.add_event_day_calendar.setOnClickListener {

                val EVENT_BEGIN_TIME_IN_MILLIS = Calendar.getInstance().timeInMillis
                val EVENT_END_TIME_IN_MILLIS = Calendar.getInstance().timeInMillis+3600000
                val descriptionEvent = formatDescriptionForEvent(day)

                val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, day.name)
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,EVENT_BEGIN_TIME_IN_MILLIS)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,EVENT_END_TIME_IN_MILLIS)
                    .putExtra(CalendarContract.Events.DESCRIPTION, descriptionEvent)
                    .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE)
                startActivity(context, insertCalendarIntent, null)
            }
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
    private fun formatDescriptionForEvent(day: Day) : String{
        var res = "Exercises to be done:\n"
        day.activities.forEach {
            res += it.exercise.title + "\n"
        }
        return res
    }
}