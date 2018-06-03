package com.example.newui_smartdrawer


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.fragment_drawer.*

class DrawerFragment : Fragment() {
    private  var drawerID  = 0
    private  var dbManager:DBManager?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        if(arguments!=null)
        {
            drawerID = arguments.getInt("drawerID")
            tv_Fdrawer_drawerNum.text=("抽屉"+drawerID)
           val status =  dbManager!!.getDrawerByDrawerId(drawerID,1).getStatue()
            if(status == "1")
            {
                tv_Fdrawer_use.text = "暂时禁止"
//                iv_Fdrawer_use.setImageResource()
                //状态颜色由红色变为绿色
            }

        }
        ib_Fdrawer_drawer.setOnClickListener({
            val intent = Intent()
            intent.setClass(context,SetDrawerActivity::class.java)
            intent.putExtra("set_drawerId",drawerID.toString())
            startActivityForResult(intent,1)


        })


    }

}
