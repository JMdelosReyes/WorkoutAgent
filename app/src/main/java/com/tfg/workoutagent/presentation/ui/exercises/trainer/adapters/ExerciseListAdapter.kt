package com.tfg.workoutagent.presentation.ui.exercises.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Exercise
import com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments.ExerciseTrainerFragmentDirections
import kotlinx.android.synthetic.main.item_row.view.*

class ExerciseListAdapter(private val context: Context) :
    RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>() {

    private var dataList = mutableListOf<Exercise>()
    fun setListData(data: MutableList<Exercise>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)
        return ExerciseListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        val exercise: Exercise = dataList[position]
        holder.bindView(exercise)
    }

    inner class ExerciseListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(exercise: Exercise) {
            var tagText = ""
            for ((i, tag) in exercise.tags.withIndex()) {
                if (i < exercise.tags.size - 1) {
                    tagText = "$tagText$tag, "
                } else {
                    tagText += tag
                }
            }
            Glide.with(context).load(exercise.photos[0])
                .into(itemView.circleImageViewExercise)
            itemView.row_exercise_title.text = exercise.title
            itemView.row_exercise_description.text = tagText
            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    ExerciseTrainerFragmentDirections.actionNavigationExercisesTrainerToDisplayExercise(
                        exercise.id, exercise.title
                    )
                )
            }
        }
    }
}