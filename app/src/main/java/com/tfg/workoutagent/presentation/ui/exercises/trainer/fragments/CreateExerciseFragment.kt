package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentCreateExerciseBinding
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.CreateExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.CreateExerciseViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class CreateExerciseFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, CreateExerciseViewModelFactory(
                ManageExerciseUseCaseImpl(ExerciseRepositoryImpl())
            )
        ).get(CreateExerciseViewModel::class.java)
    }

    private lateinit var binding: FragmentCreateExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_exercise,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel.titleError.observe(viewLifecycleOwner, Observer {
            binding.exerciseTitleInputEdit.error =
                if (it != "") it else null
        })

        viewModel.descriptionError.observe(viewLifecycleOwner, Observer {
            binding.exerciseDescriptionInputEdit.error =
                if (it != "") it else null
        })

        viewModel.tagsError.observe(viewLifecycleOwner, Observer {
            binding.exerciseTagsInputEdit.error =
                if (it != "") it else null
        })

        viewModel.exerciseCreated.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    true -> {
                        Toast.makeText(context, "Exercise created", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(CreateExerciseFragmentDirections.actionCreateExerciseFragmentToNavigationExercisesTrainer())
                    }
                    false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}
