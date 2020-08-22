package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseAdapterInterface
import com.tfg.workoutagent.models.Customer
import kotlinx.android.synthetic.main.item_customer_assign_routine.view.*

class CustomerListAdapter(
    private val context: Context,
    private val clickListener: (customer: Customer, view: View) -> Unit
) :
    RecyclerView.Adapter<CustomerListAdapter.CustomerListViewHolder>(),
    BaseAdapterInterface {

    private var dataList = mutableListOf<Customer>()
    fun setListData(data: MutableList<Customer>) {
        this.dataList = data
        if (!this.dataList.contains(this.selectedCustomer)) {
            this.setSelectedCustomer(null)
        }
        notifyDataSetChanged()
    }

    private var selectedCustomer: Customer? = null
    fun setSelectedCustomer(customer: Customer?) {
        this.selectedCustomer = customer
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerListAdapter.CustomerListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_customer_assign_routine, parent, false)
        return CustomerListViewHolder(view)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(
        holder: CustomerListAdapter.CustomerListViewHolder,
        position: Int
    ) {
        holder.bindView(dataList[position])
    }

    inner class CustomerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var customer: Customer

        @SuppressLint("SetTextI18n")
        fun bindView(customer: Customer) {
            this.customer = customer
            itemView.customer_assign_routine_name.text =
                this.customer.name + "\n" + this.customer.surname
            this.loadImage()
            this.loadBackGround()

            itemView.setOnClickListener {
                clickListener(this.customer, itemView)
                notifyDataSetChanged()
            }
        }

        private fun loadImage() {
            if (this.customer.photo == "DEFAULT_IMAGE" ||  this.customer.photo == "DEFAULT_PHOTO") {
                Glide.with(context).load(R.drawable.ic_person_black_60dp)
                    .into(itemView.customer_assign_routine_photo)
            } else {
                Glide.with(context).load(this.customer.photo)
                    .error(R.drawable.ic_person_black_60dp)
                    .into(itemView.customer_assign_routine_photo)
            }
        }

        private fun loadBackGround() {
            if (selectedCustomer == null) {
                if (isDarkMode(context)) {
                    this.itemView.setBackgroundResource(R.drawable.item_border_dark)
                } else {
                    this.itemView.setBackgroundColor(Color.WHITE)
                }
            } else {
                if (selectedCustomer?.id == customer.id) {
                    this.itemView.setBackgroundResource(R.drawable.item_border_primary_color)
                } else {
                    if (isDarkMode(context)) {
                        this.itemView.setBackgroundResource(R.drawable.item_border_dark)
                    } else {
                        this.itemView.setBackgroundColor(Color.WHITE)
                    }
                }
            }
        }
    }
}