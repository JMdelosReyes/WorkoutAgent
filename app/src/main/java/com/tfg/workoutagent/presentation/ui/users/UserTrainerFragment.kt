package com.tfg.workoutagent.presentation.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.tfg.workoutagent.R
import kotlinx.android.synthetic.main.user_trainer_fragment.*

class UserTrainerFragment : Fragment() {

    private lateinit var viewModel: UserTrainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@UserTrainerFragment).get(UserTrainerViewModel::class.java)
        val root = inflater.inflate(R.layout.user_trainer_fragment, container, false)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            text_user_fragment.text = it
        })
        return root

    }
}
