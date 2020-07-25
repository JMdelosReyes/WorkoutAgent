package com.tfg.workoutagent.presentation.ui.routines.trainer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryListAdapter(
    private val context: Context,
    private val darkMode: Boolean,
    private val clickListener: (categoryName: String, categoryPosition: Int, view: View) -> Unit
) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

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

            if(darkMode){
                this.itemView.setBackgroundResource(R.drawable.item_border_dark)
            }else{
                this.itemView.setBackgroundColor(Color.WHITE)
            }
            itemView.category_name.text = this.category.name
            when(this.category.name){
                "Arms" -> {itemView.category_button.setImageResource(R.drawable.ic_arms_40dp)}
                "Back" -> {itemView.category_button.setImageResource(R.drawable.ic_back_40dp)}
                "Chest" -> {itemView.category_button.setImageResource(R.drawable.ic_chest_40dp)}
                "Legs" -> {itemView.category_button.setImageResource(R.drawable.ic_legs_40dp)}
                "Shoulder" -> {itemView.category_button.setImageResource(R.drawable.ic_shoulder_40dp)}
                "Gluteus" -> {itemView.category_button.setImageResource(R.drawable.ic_gluteus_40dp)}
                "Abs" -> {itemView.category_button.setImageResource(R.drawable.ic_abs_40dp)}
                "Cardio" -> {itemView.category_button.setImageResource(R.drawable.ic_cardio_40dp)}
            }
            itemView.setOnClickListener {
                clickListener(this.category.name, adapterPosition, it)
            }
        }
    }
}