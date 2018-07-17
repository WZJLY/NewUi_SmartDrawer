package com.example.newui_smartdrawer


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.fragment_drawer2.*
import org.greenrobot.eventbus.EventBus

class DrawerFragment2 : Fragment() {
    private var scApp:SCApp?=null
    private var drawerId= 0
    private var dbManager:DBManager?=null
    private var activityCallback:DrawerFragment2.updateDrawerlisten? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        if(arguments!=null)
        {
            drawerId =  arguments.getInt("drawerID")
            tv_Fdrawer2_drawerNum.text="抽屉"+drawerId
            val statue =  dbManager!!.getDrawerByDrawerId(drawerId,scApp!!.boxId).getStatue()
            if(statue=="1")
            {
                ib_Fdrawer2_op.isEnabled=false
                //改变底色
            }

        }
        if(scApp?.touchdrawer==drawerId)
        {
            scApp?.touchdrawer=0
            val tableFragment = TableFragment()
            val args = Bundle()
            args.putInt("drawerID", drawerId)
            args.putString("table","operation")
            tableFragment.arguments=args
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fl_Fdrawer2_table,tableFragment,"table")
            fragmentTransaction.commit()
        }

        ib_Fdrawer2_op.setOnClickListener{
            if (childFragmentManager.findFragmentByTag("table") == null) {
                scApp?.touchdrawer = drawerId
                updateDrawerClicked("update")
            } else {
                val tableFragment = childFragmentManager.findFragmentByTag("table")
                val fragmentTransaction = childFragmentManager.beginTransaction()
                fragmentTransaction.remove(tableFragment)
                fragmentTransaction.commit()
                val eventMessenge = BtnEvent()
                eventMessenge.setMsg("return")
                EventBus.getDefault().postSticky(eventMessenge)
            }
        }
    }
    interface updateDrawerlisten {
        fun updateDrawerClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as updateDrawerlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun updateDrawerClicked(text: String) {
        activityCallback?.updateDrawerClick(text)
    }


}
