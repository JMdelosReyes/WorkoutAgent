package com.tfg.workoutagent.presentation.ui.routines.customer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.presentation.ui.routines.customer.fragments.ListHistoricRoutinesCustomerFragmentDirections
import kotlinx.android.synthetic.main.item_row_routine.view.*

class ListHistoricRoutinesCustomerAdapter(private val context: Context) : RecyclerView.Adapter<ListHistoricRoutinesCustomerAdapter.ListHistoricRoutinesCustomerViewHolder>(){

    private var dataList = mutableListOf<Routine>()
    fun setListData(data: MutableList<Routine>) {
        dataList = data
    }

    override fun onBindViewHolder(holder: ListHistoricRoutinesCustomerViewHolder, position: Int) {
        val routine = dataList[position]
        holder.bindView(routine)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListHistoricRoutinesCustomerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_routine, parent, false)
        return ListHistoricRoutinesCustomerViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ListHistoricRoutinesCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(routine: Routine) {
            itemView.row_routine_title.text = routine.title
            itemView.row_routine_customer_name.text = (routine.customer?.name ?: "") +" "+ (routine.customer?.surname ?: "")
            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    ListHistoricRoutinesCustomerFragmentDirections.actionListHistoricRoutinesCustomerFragmentToListHistoricDaysCustomerFragment(routine.id, routine.title)
                )
            }
        }
    }
}