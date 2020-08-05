package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseAdapterInterface
import com.tfg.workoutagent.models.Routine
import kotlinx.android.synthetic.main.item_routine_assign_routine.view.*

class RoutineAssignListAdapter(
    private val context: Context,
    private val clickListener: () -> Unit
) :
    RecyclerView.Adapter<RoutineAssignListAdapter.RoutineAssignListViewHolder>(),
    BaseAdapterInterface {

    private var dataList = mutableListOf<Routine>()
    fun setListData(data: MutableList<Routine>) {
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoutineAssignListAdapter.RoutineAssignListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_routine_assign_routine, parent, false)
        return RoutineAssignListViewHolder(view)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(
        holder: RoutineAssignListAdapter.RoutineAssignListViewHolder,
        position: Int
    ) {
        holder.bindView(dataList[position])
    }

    inner class RoutineAssignListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var routine: Routine

        fun bindView(routine: Routine) {
            this.routine = routine
            itemView.routine_assign_routine_title.text = this.routine.title
        }
    }
}
