package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.base.BaseAdapterInterface
import com.tfg.workoutagent.models.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryListAdapter(
    private val context: Context,
    private val clickListener: (categoryName: String, categoryPosition: Int, view: View) -> Unit
) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>(), BaseAdapterInterface {

    private var dataList = mutableListOf<Category>()
    fun setListData(data: MutableList<Category>) {
        this.dataList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListAdapter.CategoryListViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.item_category, parent, false)
        return CategoryListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    override fun onBindViewHolder(
        holder: CategoryListAdapter.CategoryListViewHolder,
        position: Int
    ) {
        holder.bindView(dataList[position])
    }

    inner class CategoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var category: Category

        fun bindView(category: Category) {
            this.category = category

            if (isDarkMode(context)) {
                this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
            } else {
                this.itemView.setBackgroundResource(R.drawable.item_border_unselected)
            }

            itemView.category_name.text = this.category.name
            itemView.category_button.setImageResource(this.category.icon)

            itemView.setOnClickListener {
                clickListener(this.category.name, adapterPosition, it)
            }
        }
    }
}