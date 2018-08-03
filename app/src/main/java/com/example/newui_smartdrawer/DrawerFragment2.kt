package com.example.newui_smartdrawer


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SerialPortInterface
import kotlinx.android.synthetic.main.fragment_drawer2.*
import org.greenrobot.eventbus.EventBus

class DrawerFragment2 : Fragment() {
    private var scApp:SCApp?=null
    private var drawerId= 0
    private var dbManager:DBManager?=null
    private var spi: SerialPortInterface?= null
    private var activityCallback:DrawerFragment2.updateDrawerlisten? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        spi =  scApp?.getSpi()
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
            Log.d("cly",""+scApp?.touchdrawer)
//            scApp?.touchdrawer=0
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
                scApp?.touchtable=0
                scApp?.touchdrawer = drawerId
                updateDrawerClicked("update")
            } else {
                scApp?.touchtable=0
                val tableFragment = childFragmentManager.findFragmentByTag("table")
                val fragmentTransaction = childFragmentManager.beginTransaction()
                fragmentTransaction.remove(tableFragment)
                fragmentTransaction.commit()
                val eventMessenge = BtnEvent()
                eventMessenge.setMsg("return")
                EventBus.getDefault().postSticky(eventMessenge)
            }
        }
        ib_Fdrawer2_lock.setOnClickListener {
            val check =  checkLock(scApp!!.touchCabint,2)
            when(check)
            {
                -1 ->
                     Toast.makeText(context.applicationContext,"串口通讯异常",Toast.LENGTH_SHORT).show()
                0->
                {
                    spi?.sendOpenLock(scApp!!.touchCabint,tv_Fdrawer2_drawerNum.text.substring(2,3).toInt())
                }
                else ->
                {
                    Toast.makeText(context.applicationContext," 请关闭抽屉" +
                            ""+check,Toast.LENGTH_SHORT).show()
                }


            }


        }
    }

    fun checkLock(DID: Int,num: Int): Int? {
        var time = 0
        for(i in 1..num) {
            val lockData = spi?.sendGetStat(DID)
            if (lockData != null) {
                val drawerId = lockData.indexOf("1")+1
                Log.d("SubOperation",""+drawerId)
                if (drawerId > 0)
                    return drawerId
                else
                    continue
            }
            else {
                if (time > 0){
                    return -1     //串口通讯存在问题
                }
                else{
                    time++
                    continue
                }
            }
        }
        return 0                //所有的抽屉均已关上
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
