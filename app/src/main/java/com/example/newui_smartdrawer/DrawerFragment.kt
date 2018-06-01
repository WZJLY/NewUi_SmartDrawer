package com.example.newui_smartdrawer


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
        }


    }

}
