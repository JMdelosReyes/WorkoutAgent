package com.tfg.workoutagent.presentation.ui.exercises.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repoimpl.ExerciseRepoImpl
import com.tfg.workoutagent.domain.exercises.DisplayUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.viewmodels.DisplayExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.viewmodels.DisplayExerciseViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_display_exercise.*


/**
 * A simple [Fragment] subclass.
 */
class DisplayExercise : Fragment() {

    private val exerciseId by lazy { DisplayExerciseArgs.fromBundle(arguments!!).exerciseId }
    // private val exerciseTitle by lazy { DisplayExerciseArgs.fromBundle(arguments!!).exerciseTitle }

    private val viewModel by lazy {
        ViewModelProvider(
            this, DisplayExerciseViewModelFactory(
                exerciseId,
                DisplayUseCaseImpl(ExerciseRepoImpl())
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
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    exercise_title.text = it.data.title
                    exercise_description.text = it.data.description
                    exercise_tags.text = it.data.tags.joinToString()
                    Glide.with(this).load(it.data.photos[0])
                        .into(exercise_image)
                }
                is Resource.Failure -> {
                    // TODO
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun setupButtons() {
        exercise_edit_button.setOnClickListener {
            // TODO
        }
    }
}
