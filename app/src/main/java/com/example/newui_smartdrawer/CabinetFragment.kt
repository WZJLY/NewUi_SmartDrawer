package com.example.newui_smartdrawer


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.fragment_cabinet.*
import kotlinx.android.synthetic.main.fragment_set_cabinet.*


class CabinetFragment : Fragment() {
    var dbManager:DBManager?=null
    var activityCallback:CabinetFragment.deletCabinetlisten? = null
    var scApp:SCApp?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cabinet, container, false)
    }

    interface deletCabinetlisten {
        fun deletCabinetClick(text: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletCabinetlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }

    private fun deletCabinetClicked(text: String) {
        activityCallback?.deletCabinetClick(text)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        scApp= context.applicationContext as SCApp
        if(arguments!=null) {
//            if (arguments.getString("delCabinet") == "false")
//                ib_Fcabinet_del.isEnabled = false
//            if (arguments.getString("cabinetNum") != null)
//                tv_Fcabinet_num.text = arguments.getString("cabinetNum")
//            if (arguments.getString("choice") == "true") {
//                val params = LinearLayout.LayoutParams(60, 156)
//                btn_Fcabinet_cabinet.layoutParams = params
//                im_Fcabinet.setBackgroundResource(R.drawable.circle_a2a2a2)
//            }
            val boxID =  arguments.getString("boxID")
            tv_Fcabinet_num.text = "柜"+boxID
            val arrlistCabinet = dbManager!!.boxes
            if(arrlistCabinet[boxID.toInt()-1].id == scApp?.boxId)
            {

                val params = LinearLayout.LayoutParams(60, 156)
                btn_Fcabinet_cabinet.layoutParams = params
                im_Fcabinet.setBackgroundResource(R.drawable.circle_a2a2a2)

            }
        }
        ib_Fcabinet_del.setOnClickListener{             //删除必须最后一个且没有试剂存在
            val cabinetID = tv_Fcabinet_num.text.substring(0,1)
            if(dbManager!!.getReagentByboxID(cabinetID).size<=0)
            {
                if(cabinetID.toInt()==dbManager!!.boxes.size) {
                    dbManager?.deleteBox(tv_Fcabinet_num.text.substring(0, 1).toInt())
                    deletCabinetClicked("deletCabinet")
                }
                Toast.makeText(context.applicationContext,"该列柜子不是最后一列柜子无法删除",Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(context.applicationContext,"该列柜子存在试剂无法删除",Toast.LENGTH_SHORT).show()

            }

        }

        btn_Fcabinet_cabinet.setOnClickListener {
            val params = LinearLayout.LayoutParams(60, 156)
            btn_Fcabinet_cabinet.layoutParams = params
            im_Fcabinet.setBackgroundResource(R.drawable.circle_a2a2a2)
            scApp?.boxId=tv_Fcabinet_num.text.substring(0, 1).toInt()
            deletCabinetClicked("updatedrawer")
        }

    }


}
