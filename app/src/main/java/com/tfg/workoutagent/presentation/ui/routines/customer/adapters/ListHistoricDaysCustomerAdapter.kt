package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Day
import com.tfg.workoutagent.presentation.ui.routines.customer.fragments.ListHistoricDaysCustomerFragmentDirections
import kotlinx.android.synthetic.main.item_row_routine_day_customer.view.*

class ListHistoricDaysCustomerAdapter(private val routineId: String, private val context: Context) : RecyclerView.Adapter<ListHistoricDaysCustomerAdapter.ListHistoricDaysCustomerViewHolder>(){

    private var dataList = mutableListOf<Day>()
    fun setListData(data: MutableList<Day>) {
        dataList = data
    }

    override fun onBindViewHolder(holder: ListHistoricDaysCustomerViewHolder, position: Int) {
        val day = dataList[position]
        holder.bindView(day, position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListHistoricDaysCustomerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_routine_day_customer, parent, false)
        return ListHistoricDaysCustomerViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ListHistoricDaysCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(day: Day, position: Int) {
            itemView.row_routine_day_name_customer.text = day.name
            itemView.add_event_day_calendar.visibility = View.GONE
            itemView.setOnClickListener {
                itemView.findNavController().navigate(ListHistoricDaysCustomerFragmentDirections.actionListHistoricDaysCustomerFragmentToListHistoricActivitiesCustomerFragment(day.name, position, routineId))
               }
        }
    }
}