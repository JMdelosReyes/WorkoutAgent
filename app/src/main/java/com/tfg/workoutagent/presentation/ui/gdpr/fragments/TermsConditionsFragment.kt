package com.tfg.workoutagent.presentation.ui.gdpr.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.tfg.workoutagent.R
import com.tfg.workoutagent.presentation.ui.gdpr.adapters.TermsAdapter
import com.tfg.workoutagent.vo.getAllTerms
import kotlinx.android.synthetic.main.fragment_terms_and_conditions.*

class TermsConditionsFragment : Fragment() {

    /*private var activeButton1: Boolean = false
    private var activeButton2: Boolean = false
    private var activeButton3: Boolean = false
    private var activeButton4: Boolean = false
    private var activeButton5: Boolean = false
    private var activeButton6: Boolean = false
    private var activeButton7: Boolean = false
    private var activeButton8: Boolean = false
    private var activeButton9: Boolean = false
    private var activeButton10: Boolean = false
    private var activeButton11: Boolean = false
    private var activeButton12: Boolean = false
    private var activeButton13: Boolean = false
    private var activeButton14: Boolean = false
    private var activeButton15: Boolean = false
    private var activeButton16: Boolean = false
    private var activeButton17: Boolean = false*/

    private lateinit var termsAdapter: TermsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_terms_and_conditions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTermsRecycler()
    }

    private fun setupTermsRecycler() {
        this.termsAdapter = TermsAdapter(this.requireContext())
        recycler_terms.layoutManager = LinearLayoutManager(this.requireContext())
        recycler_terms.adapter = this.termsAdapter
        this.termsAdapter.setListData(getAllTerms(this.requireContext()))
    }

    /*private fun setupUI() {
        h1.setOnClickListener { changeValue(activeButton1, 1) }
        h2.setOnClickListener { changeValue(activeButton2, 2) }
        h3.setOnClickListener { changeValue(activeButton3, 3) }
        h4.setOnClickListener { changeValue(activeButton4, 4) }
        h5.setOnClickListener { changeValue(activeButton5, 5) }
        h6.setOnClickListener { changeValue(activeButton6, 6) }
        h7.setOnClickListener { changeValue(activeButton7, 7) }
        h8.setOnClickListener { changeValue(activeButton8, 8) }
        h9.setOnClickListener { changeValue(activeButton9, 9) }
        h10.setOnClickListener { changeValue(activeButton10, 10) }
        h11.setOnClickListener { changeValue(activeButton11, 11) }
        h12.setOnClickListener { changeValue(activeButton12, 12) }
        h13.setOnClickListener { changeValue(activeButton13, 13) }
        h14.setOnClickListener { changeValue(activeButton14, 14) }
        h15.setOnClickListener { changeValue(activeButton15, 15) }
        h16.setOnClickListener { changeValue(activeButton16, 16) }
        h17.setOnClickListener { changeValue(activeButton17, 17) }
        ll_p1.visibility = View.GONE
        ll_p2.visibility = View.GONE
        ll_p3.visibility = View.GONE
        ll_p4.visibility = View.GONE
        ll_p5.visibility = View.GONE
        ll_p6.visibility = View.GONE
        ll_p7.visibility = View.GONE
        ll_p8.visibility = View.GONE
        ll_p9.visibility = View.GONE
        ll_p10.visibility = View.GONE
        ll_p11.visibility = View.GONE
        ll_p12.visibility = View.GONE
        ll_p13.visibility = View.GONE
        ll_p14.visibility = View.GONE
        ll_p15.visibility = View.GONE
        ll_p16.visibility = View.GONE
        ll_p17.visibility = View.GONE
    }*/

    /*private fun changeValue(activeButton: Boolean, i: Int) {
        if (i == 1) {
            if (activeButton) {
                ll_p1.visibility = View.GONE
                activeButton1 = !activeButton
            } else {
                ll_p1.visibility = View.VISIBLE
                activeButton1 = !activeButton
            }
        } else if (i == 2) {
            if (activeButton) {
                ll_p2.visibility = View.GONE
                activeButton2 = !activeButton
            } else {
                ll_p2.visibility = View.VISIBLE
                activeButton2 = !activeButton
            }
        } else if (i == 3) {
            if (activeButton) {
                ll_p3.visibility = View.GONE
                activeButton3 = !activeButton
            } else {
                ll_p3.visibility = View.VISIBLE
                activeButton3 = !activeButton
            }
        } else if (i == 4) {
            if (activeButton) {
                ll_p4.visibility = View.GONE
                activeButton4 = !activeButton
            } else {
                ll_p4.visibility = View.VISIBLE
                activeButton4 = !activeButton
            }
        } else if (i == 5) {
            if (activeButton) {
                ll_p5.visibility = View.GONE
                activeButton5 = !activeButton
            } else {
                ll_p5.visibility = View.VISIBLE
                activeButton5 = !activeButton
            }
        } else if (i == 6) {
            if (activeButton) {
                ll_p6.visibility = View.GONE
                activeButton6 = !activeButton
            } else {
                ll_p6.visibility = View.VISIBLE
                activeButton6 = !activeButton
            }
        } else if (i == 7) {
            if (activeButton) {
                ll_p7.visibility = View.GONE
                activeButton7 = !activeButton
            } else {
                ll_p7.visibility = View.VISIBLE
                activeButton7 = !activeButton
            }
        } else if (i == 8) {
            if (activeButton) {
                ll_p8.visibility = View.GONE
                activeButton8 = !activeButton
            } else {
                ll_p8.visibility = View.VISIBLE
                activeButton8 = !activeButton
            }
        } else if (i == 9) {
            if (activeButton) {
                ll_p9.visibility = View.GONE
                activeButton9 = !activeButton
            } else {
                ll_p9.visibility = View.VISIBLE
                activeButton9 = !activeButton
            }
        } else if (i == 10) {
            if (activeButton) {
                ll_p10.visibility = View.GONE
                activeButton10 = !activeButton
            } else {
                ll_p10.visibility = View.VISIBLE
                activeButton10 = !activeButton
            }
        } else if (i == 11) {
            if (activeButton) {
                ll_p11.visibility = View.GONE
                activeButton11 = !activeButton
            } else {
                ll_p11.visibility = View.VISIBLE
                activeButton11 = !activeButton
            }
        } else if (i == 12) {
            if (activeButton) {
                ll_p12.visibility = View.GONE
                activeButton12 = !activeButton
            } else {
                ll_p12.visibility = View.VISIBLE
                activeButton12 = !activeButton
            }
        } else if (i == 13) {
            if (activeButton) {
                ll_p13.visibility = View.GONE
                activeButton13 = !activeButton
            } else {
                ll_p13.visibility = View.VISIBLE
                activeButton13 = !activeButton
            }
        } else if (i == 14) {
            if (activeButton) {
                ll_p14.visibility = View.GONE
                activeButton14 = !activeButton
            } else {
                ll_p14.visibility = View.VISIBLE
                activeButton14 = !activeButton
            }
        } else if (i == 15) {
            if (activeButton) {
                ll_p15.visibility = View.GONE
                activeButton15 = !activeButton
            } else {
                ll_p15.visibility = View.VISIBLE
                activeButton15 = !activeButton
            }
        } else if (i == 16) {
            if (activeButton) {
                ll_p16.visibility = View.GONE
                activeButton16 = !activeButton
            } else {
                ll_p16.visibility = View.VISIBLE
                activeButton16 = !activeButton
            }
        } else if (i == 17) {
            if (activeButton) {
                ll_p17.visibility = View.GONE
                activeButton17 = !activeButton
            } else {
                ll_p17.visibility = View.VISIBLE
                activeButton17 = !activeButton
            }
        }
    }*/
}
