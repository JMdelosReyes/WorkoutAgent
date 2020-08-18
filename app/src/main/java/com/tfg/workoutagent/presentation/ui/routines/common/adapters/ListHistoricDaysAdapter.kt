package com.tfg.workoutagent.presentation.ui.routines.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.presentation.ui.routines.common.fragments.ListHistoricDaysFragmentDirections
import kotlinx.android.synthetic.main.item_row_routine.view.*
import kotlinx.android.synthetic.main.item_row_routine_day_customer.view.*

class ListHistoricDaysAdapter(private val routineId: String, private val context: Context) : RecyclerView.Adapter<ListHistoricDaysAdapter.ListHistoricRoutinesViewHolder>(){

    private var dataList = mutableListOf<Day>()
    fun setListData(data: MutableList<Day>) {
        dataList = data
    }

    override fun onBindViewHolder(holder: ListHistoricRoutinesViewHolder, position: Int) {
        val day = dataList[position]
        holder.bindView(day, position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListHistoricRoutinesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_routine_day_customer, parent, false)
        return ListHistoricRoutinesViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ListHistoricRoutinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(day: Day, position: Int) {
            itemView.row_routine_day_name_customer.text = day.name
            itemView.add_event_day_calendar.visibility = View.GONE
            itemView.setOnClickListener {
                itemView.findNavController().navigate(ListHistoricDaysFragmentDirections.actionListHistoricDaysFragmentToListHistoricActivitiesFragment(routineId, position, day.name))
            }
        }
    }
}