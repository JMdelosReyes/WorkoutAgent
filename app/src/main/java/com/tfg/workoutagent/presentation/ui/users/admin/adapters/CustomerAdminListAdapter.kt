package com.tfg.workoutagent.presentation.ui.users.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Customer
import com.tfg.workoutagent.presentation.ui.users.admin.fragments.UserListFragmentDirections
import kotlinx.android.synthetic.main.item_row_customer.view.*

class CustomerAdminListAdapter (private val context: Context) : RecyclerView.Adapter<CustomerAdminListAdapter.CustomerAdminListViewHolder> (){

    private var dataCustomerList = mutableListOf<Customer>()
    fun setListData(data: MutableList<Customer>){
        dataCustomerList = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAdminListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_customer, parent, false)
        return CustomerAdminListViewHolder(view)
    }

    override fun getItemCount(): Int = if (dataCustomerList.size > 0) dataCustomerList.size else 0

    override fun onBindViewHolder(holder: CustomerAdminListViewHolder, position: Int) {
        val customer : Customer = dataCustomerList[position]
        holder.bindView(customer)
    }

    inner class CustomerAdminListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(customer: Customer){
            if(customer.photo == "" || customer.photo == "DEFAULT_IMAGE"){
                Glide.with(context).load(R.drawable.ic_person_black_60dp).into(itemView.circleImageViewCustomer)
            }else{
                Glide.with(context).load(customer.photo).into(itemView.circleImageViewCustomer)
            }
            itemView.row_customer_name.text = customer.name + " " + customer.surname
            itemView.row_customer_email.text = customer.email
            itemView.row_customer_phone.text = customer.phone

            itemView.setOnClickListener {
                itemView.findNavController().navigate(UserListFragmentDirections.actionNavigationAdminUsersToDisplayCustomerAdminFragment(customerId = customer.id, customerName = customer.name + " " + customer.surname))
            }
        }
    }
}