package com.tfg.workoutagent.vo

import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Category

fun getAllCategories(): MutableList<Category> {
    return mutableListOf(
        Category("Arms", R.drawable.ic_delete_black_48dp),
        Category("Back", R.drawable.ic_delete_black_48dp),
        Category("Shoulder", R.drawable.ic_delete_black_48dp),
        Category("Legs", R.drawable.ic_delete_black_48dp),
        Category("Abs", R.drawable.ic_delete_black_48dp),
        Category("Cardio", R.drawable.ic_delete_black_48dp),
        Category("Gluteus", R.drawable.ic_delete_black_48dp),
        Category("Chest", R.drawable.ic_delete_black_48dp)
    )
}