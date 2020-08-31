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
    private val clickListener: (routine: Routine, view: View) -> Unit
) :
    RecyclerView.Adapter<RoutineAssignListAdapter.RoutineAssignListViewHolder>(),
    BaseAdapterInterface {

    private var dataList = mutableListOf<Routine>()
    fun setListData(data: MutableList<Routine>) {
        this.dataList = data
        if (!this.dataList.contains(this.selectedRoutine)) {
            this.setSelectedRoutine(null)
        }
        notifyDataSetChanged()
    }

    private var selectedRoutine: Routine? = null
    fun setSelectedRoutine(routine: Routine?) {
        this.selectedRoutine = routine
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
            this.loadBackGround()

            itemView.setOnClickListener {
                clickListener(this.routine, itemView)
                notifyDataSetChanged()
            }
        }

        private fun loadBackGround() {
            if (selectedRoutine == null) {
                if (isDarkMode(context)) {
                    this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
                }
            } else {
                if (selectedRoutine?.id == routine.id) {
                    this.itemView.setBackgroundResource(R.drawable.item_border_selected)
                } else {
                    if (isDarkMode(context)) {
                        this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
                    } else {
                        this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
                    }
                }
            }
        }
    }
}
