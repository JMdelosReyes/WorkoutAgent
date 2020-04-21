package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.models.Routine
import com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments.ExerciseTrainerFragmentDirections
import com.tfg.workoutagent.presentation.ui.routines.trainer.fragments.RoutineTrainerFragmentDirections
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.item_row_routine.view.*

class RoutineListAdapter(private val context: Context) :
    RecyclerView.Adapter<RoutineListAdapter.RoutineListViewHolder>() {

    private var dataList = mutableListOf<Routine>()
    fun setListData(data: MutableList<Routine>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_routine, parent, false)
        return RoutineListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RoutineListViewHolder, position: Int) {
        val routine: Routine = dataList[position]
        holder.bindView(routine)
    }

    inner class RoutineListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(routine: Routine) {

            itemView.row_routine_title.text = routine.title
            itemView.row_routine_customer_name.text = (routine.customer?.name ?: "") +" "+ (routine.customer?.surname ?: "")
            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    RoutineTrainerFragmentDirections.actionNavigationRoutineTrainerToDisplayRoutineFragment(
                        routine.id, routine.title
                    )
                )
            }

        }
    }
}