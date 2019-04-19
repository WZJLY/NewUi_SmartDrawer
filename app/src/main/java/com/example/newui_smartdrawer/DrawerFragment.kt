package com.example.newui_smartdrawer


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.Reagent
import kotlinx.android.synthetic.main.fragment_drawer.*

class DrawerFragment : Fragment() {
    private  var drawerID  = 0
    private  var dbManager:DBManager?=null
    private  var reagent:Reagent?=null
    private  var scApp:SCApp?=null
    var activityCallback:DrawerFragment.deletDrawerlisten? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false)
    }
    interface deletDrawerlisten {
        fun deletDrawerClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletDrawerlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun delettemplateClicked(text: String) {
        activityCallback?.deletDrawerClick(text)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp
        if(arguments!=null)
        {
            drawerID = arguments.getInt("drawerID")
            tv_Fdrawer_drawerNum.text=("抽屉"+drawerID)
            val status =  dbManager!!.getDrawerByDrawerId(drawerID,scApp!!.boxId).statue
            if(status == "1")
            {
                tv_Fdrawer_use.text = "停止使用"
                iv_Fdrawer_use.setImageResource(R.drawable.circle_fd7f60)
                //状态颜色由红色变为绿色
            }

        }
        ib_Fdrawer_drawer.setOnClickListener{
            val intent = Intent()
            intent.setClass(context,SetDrawerActivity::class.java)
            intent.putExtra("set_drawerId",drawerID.toString())
            startActivityForResult(intent,1)


        }
        ib_Fdrawer_del.setOnClickListener{

            deletDrawer()

        }


    }
    private fun deletDrawer()
    {
        val arrListReagent = dbManager?.reagents
        val sum = arrListReagent!!.size
        var isReagent = -1
        if(sum>0) {
            for (m in 1..sum) {
                reagent = arrListReagent[m - 1]         //修改    判断该列柜子这个抽屉试剂是否存在
                if(reagent!!.drawerId.toInt()==drawerID&&reagent!!.reagentCabinetId.toInt() == scApp?.boxId)
                {
                    isReagent=1
                    break
                }

            }
            if(isReagent!=1&&dbManager!!.getDrawersByboxID(scApp!!.boxId.toString()).size==drawerID)
            {
                dbManager?.deleteDrawer(drawerID,scApp!!.boxId)
                delettemplateClicked("deletDrawer")
            }
        }
        else
        {
            if(dbManager!!.getDrawersByboxID(scApp!!.boxId.toString()).size==drawerID)
                dbManager?.deleteDrawer(drawerID,scApp!!.boxId)
            delettemplateClicked("deletDrawer") //如果没有不存在试剂，则也只能从最后一个开始删除抽屉

        }
    }       //通过判断该抽屉里是否有试剂，是否是最后一个抽屉，否则无法删除

}
