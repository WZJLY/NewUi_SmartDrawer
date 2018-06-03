package com.example.newui_smartdrawer


import android.graphics.Color
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
        if(arguments!=null)
        {   var tableNum = 0
            if(arguments.getString("status")=="set") {
                tableNum = arguments.getInt("tableNum")
            }
            if(arguments.getString("status")=="op")
            {
                tableNum=dbManager!!.getDrawerByDrawerId(arguments.getInt("drawerID"),1).drawerSize
            }
            addNum(tableNum)

        }


    }

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
                button.setBackgroundResource(R.drawable.btn_table_f6f6f6_style)
                button.setTextColor(context.resources.getColor(R.color.tb_text))
                val arrListReagent = dbManager?.reagents
                val sum = arrListReagent!!.size
                if(sum>0) {
                    for (m in 1..sum) {
                        reagent = arrListReagent[m - 1]
                        if(reagent!!.drawerId.toInt()==drawerID&&reagent!!.reagentPosition.toInt()==button.id)
                        {  if(reagent!!.reagentName.length>3)
                            button.text = reagent!!.reagentName.subSequence(0,3)
                        else  button.text = reagent!!.reagentName
                            if(reagent?.status==1) {

//                                button.setBackgroundResource(R.drawable.btn_style1)
                            }
                            if(reagent?.status==2)
                            {

//                                button.setBackgroundResource(R.drawable.btn_style2)
                            }

                        }

                    }
                }
                button.setOnClickListener { view->
                    view.isFocusable = true
                    view.requestFocus()
                    view.requestFocusFromTouch()
                    var row = button.id.toString()
                    val operationActivity = activity as OperationActivity
                    if(dbManager?.getReagentByPos(drawerID.toString(),row)!=null)
                    {
//                        if(dbManager!!.getReagentByPos(drawerID.toString(),row).status==1)
//                           operationActivity.changeMessage("style1")
//                        else
//                            operationActivity.changeMessage("style2")

                        val informationFragment = InformationFragment()
                        val fragmentTrasaction = childFragmentManager.beginTransaction()
                        val arg = Bundle()
                        arg.putString("tablenum",drawerID.toString())
                        arg.putString("pos",row)
                        informationFragment.arguments = arg
                        fragmentTrasaction.replace(R.id.fl_Ftable_information, informationFragment, "Info")
                        fragmentTrasaction.commit()

                    }
                }
                tableRow.addView(button)
            }
            tl_Ftable.addView(tableRow)
        }
    }
}
