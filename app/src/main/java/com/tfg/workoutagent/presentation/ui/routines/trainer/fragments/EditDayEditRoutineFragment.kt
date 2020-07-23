package com.tfg.workoutagent.presentation.ui.routines.trainer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.ExerciseRepositoryImpl
import com.tfg.workoutagent.data.repositoriesImpl.RoutineRepositoryImpl
import com.tfg.workoutagent.databinding.FragmentEditDayEditRoutineBinding
import com.tfg.workoutagent.domain.routineUseCases.ManageRoutineUseCaseImpl
import com.tfg.workoutagent.models.RoutineActivity
import com.tfg.workoutagent.presentation.ui.routines.trainer.adapters.ActivityListAdapter
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.EditRoutineViewModel
import com.tfg.workoutagent.presentation.ui.routines.trainer.viewModels.EditRoutineViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_day_edit_routine.*

class EditDayEditRoutineFragment : Fragment() {

    private val routineId by lazy { EditDayEditRoutineFragmentArgs.fromBundle(requireArguments()).routineId }

    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            EditRoutineViewModelFactory(
                routineId,
                ManageRoutineUseCaseImpl(RoutineRepositoryImpl(), ExerciseRepositoryImpl())
            )
        ).get(EditRoutineViewModel::class.java)
    }

    private lateinit var adapter: ActivityListAdapter

    private lateinit var binding: FragmentEditDayEditRoutineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_day_edit_routine,
            container,
            false
        )
        this.binding.viewModel = viewModel
        this.binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActivityListAdapter(this.requireContext()) { routineActivity: RoutineActivity ->
            viewModel.onEditActivity(routineActivity)
            findNavController().navigate(
                EditDayEditRoutineFragmentDirections.actionEditDayEditRoutineFragmentToEditActivityEditRoutineFragment(
                    routineId = routineId
                )
            )
        }
        recycler_edit_day_activities.layoutManager = LinearLayoutManager(this.requireContext())
        recycler_edit_day_activities.adapter = adapter

        /*val itemTouchHelper = setUpItemTouchHelper()
        itemTouchHelper.attachToRecyclerView(recycler_edit_day_activities)*/

        // TODO
        viewModel.adapter = adapter

        onBackPressed()
        observeData()
        observeErrors()
        setupButtons()
    }

    /*private fun setUpItemTouchHelper(): ItemTouchHelper {
        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder as ActivityListAdapter.ActivityListViewHolder
                val activity = viewHolder.activity
                viewModel.removeActivity(activity)
            }
        }

        return ItemTouchHelper(simpleItemTouchCallback)
    }*/

    private fun observeErrors() {
        viewModel.dayNameError.observe(viewLifecycleOwner, Observer {
            binding.routineDayNameInputEdit.error =
                if (it != "") it else null
        })

        viewModel.activitiesError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == "") {
                    binding.dayActivitiesError.visibility = View.INVISIBLE
                } else {
                    binding.dayActivitiesError.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        viewModel.activities.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.setListData(it)
                adapter.notifyDataSetChanged()
                // findNavController().navigate(CreateRoutineFragmentDirections.actionCreateRoutineToAddDayFragment())
            }
        })

        viewModel.dayCreated.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(
                        EditDayEditRoutineFragmentDirections.actionEditDayEditRoutineFragmentToEditRoutineFragment(
                            clearData = 2,
                            routineId = routineId
                        )
                    )
                }
            }
        })
    }

    private fun onBackPressed() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                findNavController().navigate(
                    EditDayEditRoutineFragmentDirections.actionEditDayEditRoutineFragmentToEditRoutineFragment(
                        clearData = 2,
                        routineId = routineId
                    )
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupButtons() {
        save_create_day_button.setOnClickListener {
            viewModel.onSaveEditDay()
        }

        add_day_activity_button.setOnClickListener {
            findNavController().navigate(
                EditDayEditRoutineFragmentDirections.actionEditDayEditRoutineFragmentToAddActivityEditRoutineFragment(
                    routineId = routineId
                )
            )
        }

        /*cancel_create_day_edit_routine_button.setOnClickListener {
            viewModel.onCancelEditDay()
            findNavController().navigate(
                EditDayEditRoutineFragmentDirections.actionEditDayEditRoutineFragmentToEditRoutineFragment(
                    routineId = routineId,
                    clearData = 2
                )
            )
        }*/
    }
}
