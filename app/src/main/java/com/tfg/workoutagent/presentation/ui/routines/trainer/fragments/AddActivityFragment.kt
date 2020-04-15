package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentAddActivityBinding
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.CreateRoutineViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_activity.*


/**
 * A simple [Fragment] subclass.
 */
class AddActivityFragment : DialogFragment() {

    private lateinit var toolbar: Toolbar

    private lateinit var binding: FragmentAddActivityBinding

    private val viewModel by lazy {
        ViewModelProvider(
            activity!!, CreateRoutineViewModelFactory(
                ManageRoutineUseCaseImpl(RoutineRepositoryImpl(), ExerciseRepositoryImpl())
            )
        ).get(CreateRoutineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_activity,
            container,
            false
        )
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this
        toolbar = binding.root.findViewById(R.id.add_activity_toolbar)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupDialogWindow()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupSpinnerAdapter()
        observeErrors()
        observeDialogState()
    }

    private fun observeDialogState() {
        viewModel.closeActivityDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    dismiss()
                    viewModel.activityDialogClosed()
                }
            }
        })
    }

    private fun observeErrors() {
        viewModel.setsError.observe(viewLifecycleOwner, Observer {
            binding.activitySetInputEdit.error =
                if (it != "") it else null
        })

        viewModel.repetitionsError.observe(viewLifecycleOwner, Observer {
            binding.activityRepetitionsInputEdit.error =
                if (it != "") it else null
        })

        viewModel.weightsError.observe(viewLifecycleOwner, Observer {
            binding.activityWeightsInputEdit.error =
                if (it != "") it else null
        })

        viewModel.noteError.observe(viewLifecycleOwner, Observer {
            binding.activityNoteInputEdit.error =
                if (it != "") it else null
        })
    }

    private fun setupDialogWindow() {
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            viewModel.clearActivityData()
            dismiss()
        }
        toolbar.title = "Some Title"
        toolbar.inflateMenu(R.menu.add_day_activity_dialog_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save_day_activity -> {
                    viewModel.onSaveActivity()
                }
                else -> {
                    dismiss()
                }
            }
            true
        }
    }

    private fun setupSpinnerAdapter() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                val spinner: Spinner = activity_exercise_spinner
                ArrayAdapter(
                    this.context!!,
                    android.R.layout.simple_spinner_item,
                    it.map { e -> e.title }
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            viewModel.selectExercise(it[position])
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Code to perform some action when nothing is selected
                        }
                    }
                }
            }
        })
        viewModel.getExercises()
    }
}
