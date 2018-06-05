package com.example.newui_smartdrawer


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.Drawer
import com.example.newui_smartdrawer.util.Reagent
import kotlinx.android.synthetic.main.fragment_table.*
import org.greenrobot.eventbus.EventBus


class TableFragment : Fragment() {
    private var scApp: SCApp? = null
    var num = 0
    var drawerID = 0
    var dbManager: DBManager? = null
    private var drawer: Drawer?=null
    private var reagent: Reagent?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        if(arguments!=null) {
            var tableNum = 0
            when(arguments.getString("table")) {
                "setDrawer" -> {
                    tableNum = arguments.getInt("tableNum")
                }
                "subOperation" -> {
                    tableNum=dbManager!!.getDrawerByDrawerId(scApp!!.touchdrawer,1).drawerSize
                }
                "operation" -> {
                    drawerID=arguments.getInt("drawerID")
                    tableNum=dbManager!!.getDrawerByDrawerId(drawerID,1).drawerSize
                }
            }
            addNum(tableNum)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun addNum(num: Int){
        tl_Ftable.removeAllViews()
        val params = TableRow.LayoutParams(55, 55)
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val button = Button(context)
                button.layoutParams = params
                button.isFocusable = false
                button.id = (i-1)*num+j
                button.textSize = 12F
                //按钮底色和字体颜色
                button.setBackgroundResource(R.drawable.btn_table1_style)
                button.setTextColor(R.color.btn_table_textstyle)
                val arrListReagent = dbManager?.reagents
                val sum = arrListReagent!!.size
                when (arguments.getString("table")){
                    "setDrawer" -> {
                        button.isClickable = false
                        button.setBackgroundResource(R.drawable.btn_table1)
                    }
                    "subOperation" -> {
                        button.isClickable = false
                        if(scApp?.touchtable==button.id)
                            button.setBackgroundResource(R.drawable.btn_table1_press)
                        else
                            button.setBackgroundResource(R.drawable.btn_table1)
                    }
                    "operation" -> {
                        if(sum>0) {
                            for (m in 1..sum) {
                                reagent = arrListReagent[m - 1]
                            }
                            for (m in 1..sum) {
                                reagent = arrListReagent[m - 1]
                                if(reagent!!.drawerId.toInt()==drawerID&&reagent!!.reagentPosition.toInt()==button.id)
                                {
                                    if(scApp?.touchdrawer==drawerID&&scApp?.touchtable==button.id )
                                    {
                                        //备注：这个网格上应该有选中标点
                                        val informationFragment = InformationFragment()
                                        val fragmentTrasaction = childFragmentManager.beginTransaction()
                                        val arg = Bundle()
                                        arg.putString("showMessage","show")
                                        arg.putString("tablenum",drawerID.toString())
                                        arg.putString("op",button.id.toString())
                                        informationFragment.arguments = arg
                                        fragmentTrasaction.replace(R.id.fl_Ftable_information, informationFragment, "Info")
                                        fragmentTrasaction.commit()
                                        scApp?.touchdrawer=0
                                        scApp?.touchtable =0

                                    }
                                    if(reagent!!.reagentName.length>3)
                                        button.text = reagent!!.reagentName.subSequence(0,3)
                                    else
                                        button.text = reagent!!.reagentName
                                    if(reagent?.status==1) {
                                        button.setBackgroundResource(R.drawable.btn_table2_style)
                                        //在位时得颜色
                                    }
                                    if(reagent?.status==2)
                                    {
                                        //取用时得颜色
                                        button.setBackgroundResource(R.drawable.btn_table1_style)
                                    }
                                }
                            }
                        }
                        button.setOnClickListener { view->
                            var row = button.id.toString()
                            scApp?.touchtable=row.toInt()
                            view.isFocusable = true
                            view.requestFocus()
                            view.requestFocusFromTouch()
                            if(dbManager?.getReagentByPos(drawerID.toString(),row)!=null)
                            {
                                if(dbManager!!.getReagentByPos(drawerID.toString(),row).status==1)
                                {
                                    val eventMessenge = BtnEvent()
                                    eventMessenge.setMsg("take")
                                    EventBus.getDefault().postSticky(eventMessenge)
                                }
                                else
                                {
                                    val eventMessenge = BtnEvent()
                                    eventMessenge.setMsg("return")
                                    EventBus.getDefault().postSticky(eventMessenge)
                                }
                                val informationFragment = InformationFragment()
                                val fragmentTrasaction = childFragmentManager.beginTransaction()
                                val arg = Bundle()
                                arg.putString("showMessage","show")
                                arg.putString("tablenum",drawerID.toString())
                                arg.putString("pos",row)
                                informationFragment.arguments = arg
                                fragmentTrasaction.replace(R.id.fl_Ftable_information, informationFragment, "Info")
                                fragmentTrasaction.commit()

                            }
                            else
                            {
                                val eventMessenge = BtnEvent()
                                eventMessenge.setMsg("into")
                                EventBus.getDefault().postSticky(eventMessenge)
                                if(childFragmentManager.findFragmentByTag("Info")!=null) {
                                    val informationFragment = childFragmentManager.findFragmentByTag("Info")
                                    val fragmentTrasaction = fragmentManager.beginTransaction()
                                    fragmentTrasaction.remove(informationFragment)
                                    fragmentTrasaction.commit()
                                }
                            }
                        }
                    }
                }
                tableRow.addView(button)
            }
            tl_Ftable.addView(tableRow)
        }
    }
}
