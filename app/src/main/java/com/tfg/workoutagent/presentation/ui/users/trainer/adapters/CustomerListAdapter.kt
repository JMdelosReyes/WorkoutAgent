package com.tfg.workoutagent.presentation.ui.users.trainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Customer
import kotlinx.android.synthetic.main.item_row_customer.view.*

class CustomerListAdapter(private val context: Context) :  RecyclerView.Adapter<CustomerListAdapter.CustomerListViewHolder>(){

    private var dataList = mutableListOf<Customer>()
    fun setListData(data: MutableList<Customer>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_customer, parent, false)
        return CustomerListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: CustomerListViewHolder, position: Int) {
        val customer: Customer = dataList[position]
        holder.bindView(customer)
    }

    inner class CustomerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(customer: Customer) {
            Glide.with(context).load(customer.photo)
                .into(itemView.circleImageViewCustomer)
            itemView.row_customer_name.text = customer.name
            itemView.row_customer_surname.text = customer.surname
            itemView.row_customer_email.text = customer.email
            itemView.row_customer_phone.text = customer.phone
            /*
            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    ExerciseTrainerFragmentDirections.actionNavigationExercisesTrainerToDisplayExercise(
                        exercise.id, exercise.title
                    )
                )
            }*/
        }
    }
}