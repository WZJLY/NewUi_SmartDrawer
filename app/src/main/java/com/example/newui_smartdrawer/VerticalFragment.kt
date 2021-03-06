package com.example.newui_smartdrawer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.Reagent

class VerticalFragment : Fragment() {
    private var dbManager:DBManager?=null
    private var reagent:Reagent?=null
    private var scApp:SCApp?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vertical, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        scApp = context.applicationContext as SCApp
        if(arguments!=null) {
            if (arguments.getString("addtemplate") == "add") {
                val arrListReagentTemplate = dbManager?.reagentTemplate
                val sum = arrListReagentTemplate!!.size
                if (sum > 0) {
                    for (i in 1..sum) {
                        val templateFragment = TemplateFragment()
                        val args = Bundle()
                        args.putInt("order", i - 1)
                        templateFragment.arguments = args
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.ll_Fvertical, templateFragment)
                        fragmentTransaction.commit()
                    }
                } else
                    Toast.makeText(context, "没有试剂模板", Toast.LENGTH_SHORT).show()
            }
            if (arguments.getString("addreagent") == "addreagent") {
                val arrayListReagent = dbManager?.reagents
                val serach = arguments.getString("serach")
                if (arrayListReagent != null) {
                    val sum = arrayListReagent.size
                    if (sum > 0) {
                        for (i in 1..sum) {
                            reagent = arrayListReagent.get(i - 1)
                            if(serach !=null) {
                                if (reagent!!.reagentName.contains(serach)) {
                                    val fragment = childFragmentManager.beginTransaction()
                                    val informationFragment = InformationFragment()
                                    val args = Bundle()
                                    args.putString("addreagent", "addreagent")
                                    args.putString("reagentID", reagent?.reagentId)
                                    informationFragment.arguments = args
                                    fragment.add(R.id.ll_Fvertical, informationFragment)
                                    fragment.commit()
                                }
                            }
                            else
                            {
                                val fragment = childFragmentManager.beginTransaction()
                                val informationFragment = InformationFragment()
                                val args = Bundle()
                                args.putString("addreagent", "addreagent")
                                args.putString("reagentID", reagent?.reagentId)
                                informationFragment.arguments = args
                                fragment.add(R.id.ll_Fvertical, informationFragment)
                                fragment.commit()
                            }

                        }
                    }
                }
            }

            if (arguments.getString("drawer") == "addDrawer") {

                updateDrawer()

            }
        }

    }
    fun updateDrawer()      //根据选中的列数进行更新
    {
        val arrListDrawers = dbManager?.getDrawersByboxID(scApp?.boxId.toString())
        val sum = arrListDrawers!!.size
        Log.d("sum",""+sum)
        if(sum > 0)
        {
            for (i in 1..sum){
                    val fragment = childFragmentManager.beginTransaction()
                    val drawerFragment = DrawerFragment()
                    val args = Bundle()
                    args.putInt("drawerID", i)
                    drawerFragment.arguments = args
                    fragment.add(R.id.ll_Fvertical, drawerFragment)
                    fragment.commit()
            }
        }
        else
        {
            Toast.makeText(context, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }
    }


}
