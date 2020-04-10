package com.tfg.workoutagent.presentation.ui.activity.trainer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.activity.trainer.viewModels.ActivityTrainerViewModel
import kotlinx.android.synthetic.main.fragment_trainer_activity.*

class ActivityTrainerFragment : Fragment() {

    private lateinit var activityTrainerViewModel: ActivityTrainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityTrainerViewModel =
            ViewModelProvider(this@ActivityTrainerFragment).get(ActivityTrainerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trainer_activity, container, false)
        activityTrainerViewModel.text.observe(viewLifecycleOwner, Observer {
            text_activity_fragment.text = it
        })
        return root
    }

}
