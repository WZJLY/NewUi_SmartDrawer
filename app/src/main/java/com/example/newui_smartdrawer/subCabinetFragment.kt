package com.example.newui_smartdrawer


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.fragment_sub_cabinet.*

class subCabinetFragment : Fragment() {
    var dbManager: DBManager? = null
    var activityCallback: subCabinetFragment.subUpdateDrawerlisten? = null
    var scApp: SCApp? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_cabinet, container, false)
    }

    interface subUpdateDrawerlisten {
        fun subUpdateDrawerClick(text: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as subUpdateDrawerlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }

    private fun subUpdateDrawerClicked(text: String) {
        activityCallback?.subUpdateDrawerClick(text)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager = DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp

        if (arguments != null) {
            val boxID = arguments.getInt("boxID")
            tv_Fsubcabinet_num.text = "柜" + boxID
            val arrlistCabinet = dbManager!!.boxes
            if (arrlistCabinet[boxID - 1].id == scApp?.boxId) {
                val params = LinearLayout.LayoutParams(60, 156)
                btn_Fsubcabinet_cabinet.layoutParams = params
                im_Fsubcabinet.setBackgroundResource(R.drawable.circle_a2a2a2)

            }
        }


        btn_Fsubcabinet_cabinet.setOnClickListener {
            scApp?.boxId = tv_Fsubcabinet_num.text.substring(1, 2).toInt()
            scApp?.touchtable = 0
            scApp?.touchdrawer = 0        //清0一下
            subUpdateDrawerClicked("updatedrawer")
        }


    }


}