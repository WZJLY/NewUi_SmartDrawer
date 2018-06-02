package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.fragment_cabinet.*


class CabinetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cabinet, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        if(arguments.getString("delCabinet") == "false")
            ib_Fcabinet_del.isEnabled = false
        if (arguments.getString("cabinetNum") != null)
            tv_Fcabinet_num.text = arguments.getString("cabinetNum")
        if (arguments.getString("choice") == "true"){
            val params = LinearLayout.LayoutParams(60, 156)
            im_Fcabinet_cabinet.layoutParams = params
            im_Fcabinet.setBackgroundResource(R.drawable.circle_a2a2a2)
        }
        ib_Fcabinet_del.setOnClickListener({

        })
    }


}
