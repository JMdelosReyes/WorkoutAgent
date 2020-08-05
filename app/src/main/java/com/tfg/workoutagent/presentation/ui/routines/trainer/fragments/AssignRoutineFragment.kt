package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.UserRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentAssignRoutineBinding
import com.tfg.workoutagent.domain.routineUseCases.AssignRoutinesUseCaseImpl
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.AssignRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.AssignRoutineViewModelFactory
import com.tfg.workoutagent.vo.Resource

class AssignRoutineFragment : DialogFragment() {

    private lateinit var binding: FragmentAssignRoutineBinding

    private lateinit var toolbar: Toolbar

    private val viewModel by lazy {
        ViewModelProvider(
            this, AssignRoutineViewModelFactory(
                AssignRoutinesUseCaseImpl(RoutineRepositoryImpl(), UserRepositoryImpl())
            )
        ).get(AssignRoutineViewModel::class.java)
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
            R.layout.fragment_assign_routine,
            container,
            false
        )

        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this
        this.toolbar = binding.root.findViewById(R.id.assign_routine_toolbar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onStart() {
        super.onStart()
        setupDialogWindow()
        observeData()
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
        this.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        this.toolbar.title = "Assign routine"
        this.toolbar.inflateMenu(R.menu.assign_routine_dialog_menu)
        this.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_assign_routine -> {
                    dismiss()
                }
                else -> {
                    dismiss()
                }
            }
            true
        }
    }

    private fun observeData() {
        this.viewModel.routines.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Loading -> {
                        Log.i("ROUTINES", "LOADING")
                    }
                    is Resource.Success -> {
                        Log.i("ROUTINES", "SUCCESS")
                    }
                    is Resource.Failure -> {
                        Log.i("ROUTINES", "FAILURE")
                    }
                }
            }
        })

        this.viewModel.loadRoutines()
    }
}
