package com.tfg.workoutagent.presentation.ui.exercises.trainer.fragments

import android.os.Bundle
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
import com.tfg.workoutagent.databinding.FragmentEditDeleteExerciseBinding
import com.tfg.workoutagent.domain.exerciseUseCases.ManageExerciseUseCaseImpl
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.EditDeleteExerciseViewModel
import com.tfg.workoutagent.presentation.ui.exercises.trainer.viewmodels.EditDeleteExerciseViewModelFactory
import com.tfg.workoutagent.vo.Resource

/**
 * A simple [Fragment] subclass.
 */
class EditDeleteExerciseFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, EditDeleteExerciseViewModelFactory(
                exerciseId,
                ManageExerciseUseCaseImpl(ExerciseRepositoryImpl())
            )
        ).get(EditDeleteExerciseViewModel::class.java)
    }

    private val exerciseId by lazy { EditDeleteExerciseFragmentArgs.fromBundle(arguments!!).exerciseId }
    // private val exerciseTitle by lazy { EditDeleteExerciseFragmentArgs.fromBundle(arguments!!).exerciseTitle }

    private lateinit var binding: FragmentEditDeleteExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_delete_exercise,
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
        viewModel.getExercise.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    // TODO
                    Toast.makeText(context, "Loaded successfully", Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    // TODO
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.exerciseDeleted.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Toast.makeText(context, "Exercise deleted", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EditDeleteExerciseFragmentDirections.actionEditDeleteExerciseFragmentToNavigationExercisesTrainer())
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.exerciseSaved.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Toast.makeText(context, "Exercise updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        EditDeleteExerciseFragmentDirections.actionEditDeleteExerciseFragmentToDisplayExercise(
                            exerciseId, viewModel.title.value!!
                        )
                    )
                }
                false -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

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
    }
}
