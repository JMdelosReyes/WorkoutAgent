package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseAdapterInterface
import com.tfg.workoutagent.models.Exercise
import kotlinx.android.synthetic.main.item_row_exercise_activity.view.*

class ExerciseListAdapter(
    private val context: Context,
    private val clickListener: (exercise: Exercise, view: View) -> Unit
) :
    RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>(), BaseAdapterInterface {

    private var dataList = mutableListOf<Exercise>()

    private var selectedExercise: Exercise? = null

    fun setListData(data: MutableList<Exercise>) {
        this.dataList = data
        this.notifyDataSetChanged()
    }

    fun setSelectedExercise(exercise: Exercise?) {
        this.selectedExercise = exercise
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseListAdapter.ExerciseListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_exercise_activity, parent, false)
        return ExerciseListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    override fun onBindViewHolder(
        holder: ExerciseListAdapter.ExerciseListViewHolder,
        position: Int
    ) {
        holder.bindView(dataList[position])
    }

    inner class ExerciseListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var exercise: Exercise

        fun bindView(exercise: Exercise) {
            this.exercise = exercise

            this.updateColor()

            Glide.with(context).load(this.exercise.photos[0])
                .into(itemView.exercise_activity_photo)
            itemView.exercise_activity_name.text = this.exercise.title

            itemView.setOnClickListener {
                clickListener(exercise, itemView)
                notifyDataSetChanged()
            }
        }

        private fun updateColor() {
            if (selectedExercise == null) {
                if (isDarkMode(context)) {
                    this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
                } else {
                    this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
                }
            } else {
                if (selectedExercise?.id == exercise.id) {
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