package com.tfg.workoutagent.presentation.ui.gdpr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Question
import kotlinx.android.synthetic.main.item_terms_and_conditions.view.*

class TermsAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<TermsAdapter.TermsViewHolder>() {

    private var dataList = mutableListOf<Question>()
    fun setListData(data: MutableList<Question>) {
        this.dataList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TermsAdapter.TermsViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_terms_and_conditions, parent, false)
        return TermsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    override fun onBindViewHolder(holder: TermsAdapter.TermsViewHolder, position: Int) {
        holder.bindView(dataList[position])
    }

    inner class TermsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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