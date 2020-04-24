package com.tfg.workoutagent.presentation.ui.users.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Trainer
import kotlinx.android.synthetic.main.item_row_customer.view.*

class TrainerAdminListAdapter (private val context: Context) : RecyclerView.Adapter<TrainerAdminListAdapter.TrainerAdminListViewHolder> () {

    private var dataTrainerList = mutableListOf<Trainer>()
    fun setListData(data: MutableList<Trainer>){
        dataTrainerList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainerAdminListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_customer, parent, false)
        return TrainerAdminListViewHolder(view)
    }

    override fun getItemCount(): Int = if (dataTrainerList.size > 0) dataTrainerList.size else 0

    override fun onBindViewHolder(holder: TrainerAdminListViewHolder, position: Int) {
        val trainer : Trainer = dataTrainerList[position]
        holder.bindView(trainer)
    }

    inner class TrainerAdminListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(trainer : Trainer){
            Glide.with(context).load(trainer.photo)
                .into(itemView.circleImageViewCustomer)
            itemView.row_customer_name.text = trainer.name + " " + trainer.surname
            itemView.row_customer_email.text = trainer.email
            itemView.row_customer_phone.text = trainer.phone
        }
    }
}