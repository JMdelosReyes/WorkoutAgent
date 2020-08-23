package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.domain.exerciseUseCases.DisplayExerciseUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.DisplayExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.DisplayExerciseViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_create_exercise.*
import kotlinx.android.synthetic.main.fragment_display_exercise.*
import kotlinx.android.synthetic.main.fragment_display_exercise.ll_photos


/**
 * A simple [Fragment] subclass.
 */
class DisplayExerciseFragment : Fragment() {

    private val exerciseId by lazy { DisplayExerciseFragmentArgs.fromBundle(arguments!!).exerciseId }
    private val exerciseTitle by lazy { DisplayExerciseFragmentArgs.fromBundle(arguments!!).exerciseTitle }

    private val viewModel by lazy {
        ViewModelProvider(
            this, DisplayExerciseViewModelFactory(
                exerciseId,
                DisplayExerciseUseCaseImpl(ExerciseRepositoryImpl())
            )
        ).get(DisplayExerciseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupButtons()
    }

    private fun observeData() {
        viewModel.getExercise.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO
                }
                is Resource.Success -> {
                    exercise_title.text = it.data.title
                    exercise_description.text = it.data.description
                    setPhotos(it.data.photos)
                    setTags(it.data.tags)
                }
                is Resource.Failure -> {
                    // TODO
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun setPhotos(items: MutableList<String>){
        if(items.size == 0){
            //TODO: No image available
            // val image = ImageView(this.context)
            // Glide.with(this).load(photo).into(image)
            // ll_photos.addView(image)
        } else {
            for (photo in items) {
                val image = ImageView(this.context)
                image.maxHeight = 500
                image.maxHeight = 500
                image.adjustViewBounds = true
                Glide.with(this).load(photo).into(image)
                ll_photos.addView(image)
            }
        }
    }

    private fun setTags(items: MutableList<String>){
        if(items.size == 0){
            //TODO: No image available
            // val image = ImageView(this.context)
            // Glide.with(this).load(photo).into(image)
            // ll_photos.addView(image)
        } else {
            for (tag in items) {
                val ll_vert = LinearLayout(this.context)
                ll_vert.orientation = LinearLayout.VERTICAL
                val image = ImageView(this.context)
                ll_vert.addView(image)
                ll_vert.layoutParams = LinearLayout.LayoutParams(170,170)
                image.adjustViewBounds = true
                when(tag) {
                    "Arms" -> {
                        Glide.with(this).load(R.drawable.ic_arms_40dp).into(image)
                    }
                    "Back" -> {
                        Glide.with(this).load(R.drawable.ic_back_40dp).into(image)
                    }
                    "Legs" -> {
                        Glide.with(this).load(R.drawable.ic_legs_40dp).into(image)
                    }
                    "Cardio" -> {
                        Glide.with(this).load(R.drawable.ic_cardio_40dp).into(image)
                    }
                    "Abs" -> {
                        Glide.with(this).load(R.drawable.ic_abs_40dp).into(image)
                    }
                    "Gluteus" -> {
                        Glide.with(this).load(R.drawable.ic_gluteus_40dp).into(image)
                    }
                    "Chest" -> {
                        Glide.with(this).load(R.drawable.ic_chest_40dp).into(image)
                    }
                    "Shoulder" -> {
                        Glide.with(this).load(R.drawable.ic_shoulder_40dp).into(image)
                    }
                }
                ll_tags.addView(ll_vert)
            }
        }
    }

    private fun setupButtons() {
        exercise_edit_button.setOnClickListener {
            findNavController().navigate(
                DisplayExerciseFragmentDirections.actionDisplayExerciseToEditDeleteExerciseFragment(
                    exerciseId, exerciseTitle
                )
            )
        }
    }
}
