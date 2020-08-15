package com.tfg.workoutagent.vo

import com.tfg.workoutagent.R
import com.tfg.workoutagent.models.Category

fun getAllCategories(): MutableList<Category> {
    return mutableListOf(
        Category("Arms", R.drawable.ic_arms_40dp),
        Category("Back", R.drawable.ic_back_40dp),
        Category("Shoulder", R.drawable.ic_shoulder_40dp),
        Category("Legs", R.drawable.ic_legs_40dp),
        Category("Abs", R.drawable.ic_abs_40dp),
        Category("Cardio", R.drawable.ic_cardio_40dp),
        Category("Gluteus", R.drawable.ic_gluteus_40dp),
        Category("Chest", R.drawable.ic_chest_40dp)
    )
}