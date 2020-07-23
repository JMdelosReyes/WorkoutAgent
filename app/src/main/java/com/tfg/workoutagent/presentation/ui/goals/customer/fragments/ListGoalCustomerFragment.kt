package com.tfg.workoutagent.presentation.ui.goals.customer.fragments

import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tfg.workoutagent.R
import com.tfg.workoutagent.data.repositoriesImpl.GoalRepositoryImpl
import com.tfg.workoutagent.domain.goalUseCases.ListGoalCustomerUseCaseImpl
import com.tfg.workoutagent.presentation.ui.goals.customer.adapters.GoalsCustomerListAdapter
import com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels.ListGoalCustomerViewModel
import com.tfg.workoutagent.presentation.ui.goals.customer.viewmodels.ListGoalCustomerViewModelFactory
import com.tfg.workoutagent.vo.Resource
import kotlinx.android.synthetic.main.fragment_list_goals_customer.*


class ListGoalCustomerFragment : Fragment() {

    private lateinit var adapterGoals : GoalsCustomerListAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this, ListGoalCustomerViewModelFactory(
                ListGoalCustomerUseCaseImpl(GoalRepositoryImpl())
            )
        ).get(ListGoalCustomerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_goals_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        val darkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
        adapterGoals = GoalsCustomerListAdapter(this.context!!, darkMode)
        recyclerView_goals_customer.layoutManager = LinearLayoutManager(this.context!!, LinearLayoutManager.VERTICAL, false)
        recyclerView_goals_customer.adapter = adapterGoals
        setUpItemTouchHelperGoal()
        observeData()
    }
    private fun setUpItemTouchHelperGoal() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                // we want to cache these and not allocate anything repeatedly in the onChildDraw method
                var background: Drawable? = null
                var xMark: Drawable? = null
                var xMarkMargin = 0
                var initiated = false
                private fun init() {
                    background = resources.getDrawable(R.drawable.goal_item_border_delete)
                    xMark = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_white_48dp)
                    xMark!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    xMarkMargin = this@ListGoalCustomerFragment.resources
                        .getDimension(R.dimen.ic_clear_margin).toInt()
                    initiated = true
                }

                // not important, we don't want drag & drop
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val swipedPosition = viewHolder.adapterPosition
                    val adapter: GoalsCustomerListAdapter = recyclerView_goals_customer.adapter as GoalsCustomerListAdapter
                    val builder = AlertDialog.Builder(context!!)
                    builder.setTitle("Delete this goal")
                    builder.setMessage(getString(R.string.alert_message_delete))

                    builder.setPositiveButton(getString(R.string.answer_yes)) { dialog, _ ->
                        adapterGoals.dataGoalsList.removeAt(viewHolder.adapterPosition)
                        viewModel.removeGoal(viewHolder.adapterPosition)
                        dialog.dismiss()
                    }

                    builder.setNeutralButton(getString(R.string.answer_no)) { dialog, _ ->
                        //adapterGoals.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    builder.create()
                    builder.show()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView

                    // not sure why, but this method get's called for viewholder that are already swiped away
                    if (viewHolder.adapterPosition == -1) {
                        // not interested in those
                        return
                    }
                    if (!initiated) {
                        init()
                    }

                    // draw red background
                    background!!.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background!!.draw(c)

                    // draw x mark
                    val itemHeight = itemView.bottom - itemView.top
                    val intrinsicWidth = xMark!!.intrinsicWidth
                    val intrinsicHeight = xMark!!.intrinsicWidth
                    val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                    val xMarkRight = itemView.right - xMarkMargin
                    val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val xMarkBottom = xMarkTop + intrinsicHeight
                    xMark!!.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                    xMark!!.draw(c)
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val simpleItemTouchCallback2: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                // we want to cache these and not allocate anything repeatedly in the onChildDraw method
                var background: Drawable? = null
                var xMark: Drawable? = null
                var xMarkMargin = 0
                var initiated = false
                private fun init() {
                    background = resources.getDrawable(R.drawable.goal_item_border_achieve)
                    xMark = ContextCompat.getDrawable(requireContext(), R.drawable.ic_finish_32dp)
                    xMark!!.setColorFilter(Color.HSVToColor(floatArrayOf(38F, 0.75F, 0.98F)), PorterDuff.Mode.SRC_ATOP)
                    xMarkMargin = this@ListGoalCustomerFragment.resources
                        .getDimension(R.dimen.ic_clear_margin).toInt()
                    initiated = true
                }

                // not important, we don't want drag & drop
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val swipedPosition = viewHolder.adapterPosition
                    val adapter: GoalsCustomerListAdapter = recyclerView_goals_customer.adapter as GoalsCustomerListAdapter
                    adapterGoals.dataGoalsList.removeAt(viewHolder.adapterPosition)
                    viewModel.finishGoal(viewHolder.adapterPosition)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView

                    // not sure why, but this method get's called for viewholder that are already swiped away
                    if (viewHolder.adapterPosition == -1) {
                        // not interested in those
                        return
                    }
                    if (!initiated) {
                        init()
                    }

                    // draw red background
                    background!!.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.right + dX.toInt(),
                        itemView.bottom
                    )
                    background!!.draw(c)

                    // draw x mark
                    val itemHeight = itemView.bottom - itemView.top
                    val intrinsicWidth = xMark!!.intrinsicWidth
                    val intrinsicHeight = xMark!!.intrinsicWidth
                    val xMarkLeft = itemView.left + xMarkMargin
                    val xMarkRight = itemView.left + xMarkMargin + intrinsicWidth
                    val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val xMarkBottom = xMarkTop + intrinsicHeight
                    xMark!!.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                    xMark!!.draw(c)
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        val mItemTouchHelper2 = ItemTouchHelper(simpleItemTouchCallback2)
        mItemTouchHelper.attachToRecyclerView(recyclerView_goals_customer)
        mItemTouchHelper2.attachToRecyclerView(recyclerView_goals_customer)
    }
/* Swipe alternativo
    private fun setUpItemTouchHelperGoal(): ItemTouchHelper {
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
                viewHolder as GoalsCustomerListAdapter.GoalsCustomerListViewHolder
                if(direction == ItemTouchHelper.LEFT){
                    adapterGoals.dataGoalsList.removeAt(viewHolder.adapterPosition)
                    viewModel.removeGoal(viewHolder.adapterPosition)
                }else if (direction == ItemTouchHelper.RIGHT){
                    viewModel.finishGoal(viewHolder.adapterPosition)
                }
            }
        }

        return ItemTouchHelper(simpleItemTouchCallback)
    }

 */
    private fun setupUI(){
        fab_add_goal.setOnClickListener { findNavController().navigate(ListGoalCustomerFragmentDirections.actionListGoalCustomerFragmentToCreateGoalCustomerFragment()) }
    }
    private fun observeData(){
        viewModel.goalsList.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {sfl_rv_goals_customer.startShimmer()}
                is Resource.Failure -> {sfl_rv_goals_customer.visibility = View.GONE
                    sfl_rv_goals_customer.stopShimmer()
                    Toast.makeText(this.context!!,"Something went wrong ${it.exception.message}",Toast.LENGTH_LONG).show()}
                is Resource.Success -> {
                    sfl_rv_goals_customer.visibility = View.GONE
                    sfl_rv_goals_customer.stopShimmer()
                    if(it.data.size==0){
                        //TODO: Mensaje tipo "You don't have any goals yet"
                    }
                    adapterGoals.setListData(it.data)
                    adapterGoals.notifyDataSetChanged()
                }
            }
        })
        viewModel.goalDeleted.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    adapterGoals.notifyDataSetChanged()
                    //findNavController().navigate(ListGoalCustomerFragmentDirections.actionListGoalCustomerFragmentSelf2())
                }
                false -> {
                    Log.i("observeData goalDeleted", "Something went wrong")
                }
            }
        })
        viewModel.goalFinished.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    adapterGoals.notifyDataSetChanged()
                    findNavController().navigate(ListGoalCustomerFragmentDirections.actionListGoalCustomerFragmentSelf2())
                }
                false -> {
                    Log.i("observeData goalFinishe", "Something went wrong")
                }
            }
        })
    }
}
