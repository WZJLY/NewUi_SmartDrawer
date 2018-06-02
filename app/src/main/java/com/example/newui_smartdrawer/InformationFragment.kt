package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cabinet.*
import kotlinx.android.synthetic.main.fragment_information.*

/**
 * A simple [Fragment] subclass.
 *
 */
class InformationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            if (arguments.getString("state") == "in") {
                tv_Finformation_use.visibility = View.GONE
            }
            if (arguments.getString("operation") == "operation") {
                ib_Finformation_jump.visibility = View.GONE
            }
        }
        ib_Finformation_jump.setOnClickListener {

        }
    }
}
