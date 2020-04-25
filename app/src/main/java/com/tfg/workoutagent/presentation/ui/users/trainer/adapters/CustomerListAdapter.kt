package com.tfg.workoutagent.presentation.ui.users.trainer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.presentation.ui.users.trainer.fragments.UserTrainerFragmentDirections
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
            Log.i("customer viewHolder", customer.toString())
            if(customer.photo == "" || customer.photo == "DEFAULT_IMAGE"){
                Glide.with(context).load(R.drawable.ic_person_black_60dp).into(itemView.circleImageViewCustomer)
            }else{
                Glide.with(context).load(customer.photo).into(itemView.circleImageViewCustomer)
            }
            itemView.row_customer_name.text = customer.name + " " + customer.surname
            itemView.row_customer_email.text = customer.email
            itemView.row_customer_phone.text = customer.phone

            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    UserTrainerFragmentDirections.actionNavigationUsersTrainerToDisplayCustomer(customerId = customer.id, customerName = customer.name + " " + customer.surname)
                )
            }
        }
    }
}