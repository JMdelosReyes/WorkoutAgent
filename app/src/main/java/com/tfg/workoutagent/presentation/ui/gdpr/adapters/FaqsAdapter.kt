package com.tfg.workoutagent.presentation.ui.gdpr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Question
import kotlinx.android.synthetic.main.item_faqs.view.*

class FaqsAdapter(private val context: Context) :
    RecyclerView.Adapter<FaqsAdapter.FaqsViewHolder>() {

    private var dataList = mutableListOf<Question>()
    fun setListData(data: MutableList<Question>) {
        this.dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsAdapter.FaqsViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_faqs, parent, false)
        return FaqsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FaqsAdapter.FaqsViewHolder, position: Int) {
        holder.bindView(dataList[position])
    }

    inner class FaqsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var question: Question

        fun bindView(question: Question) {
            this.question = question

            itemView.button_question.text = this.question.question
            itemView.text_answer.text = this.question.answer

            itemView.text_answer.visibility = View.GONE

            itemView.button_question.setOnClickListener {
                if (itemView.text_answer.visibility == View.GONE) {
                    itemView.text_answer.visibility = View.VISIBLE
                } else {
                    itemView.text_answer.visibility = View.GONE
                }
            }
        }
    }
}