package com.tfg.workoutagent.presentation.ui.gdpr.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.gdpr.adapters.FaqsAdapter
import com.tfg.workoutagent.vo.getAllFaqs
import kotlinx.android.synthetic.main.fragment_faqs.*

class FaqsFragment : Fragment() {

    private lateinit var faqsAdapter: FaqsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_faqs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFaqsRecycler()
    }

    private fun setupFaqsRecycler() {
        this.faqsAdapter = FaqsAdapter(this.requireContext())
        recycler_faqs.layoutManager = LinearLayoutManager(this.requireContext())
        recycler_faqs.adapter = this.faqsAdapter
        this.faqsAdapter.setListData(getAllFaqs(this.requireContext()))
    }
}
