package com.example.newui_smartdrawer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_return.*

class ReturnFragment : Fragment() {
    var activityCallback:ReturnFragment.returnbuttonlisten? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val value = arguments.getString("scan_value")
        if(value != null)
        {
            et_Freturn_code.setText(value)
            et_Freturn_load.isFocusable = true
            et_Freturn_load.isFocusableInTouchMode = true
            et_Freturn_load.requestFocus()
        }
        else {
            et_Freturn_code.isFocusable = true
            et_Freturn_code.isFocusableInTouchMode = true
            et_Freturn_code.requestFocus()
        }

        btn_Freturn_code.setOnClickListener{
            activityCallback?.returnbuttonClick("scan")
        }

        btn_Freturn_weight.setOnClickListener {
            activityCallback?.returnbuttonClick("weight")
        }

    }
    interface returnbuttonlisten {
        fun returnbuttonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as returnbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
}
