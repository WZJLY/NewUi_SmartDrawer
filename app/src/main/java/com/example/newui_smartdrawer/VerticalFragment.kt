package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager

class VerticalFragment : Fragment() {
private var dbManager:DBManager?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vertical, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        if(arguments.get("addtemplate")=="add")
        {
            val arrListReagentTemplate = dbManager?.reagentTemplate
            val sum = arrListReagentTemplate!!.size
            if(sum>0)
            {
                    for (i in 1..sum) {
                    val templateFragment = TemplateFragment()
                    val args = Bundle()
                    args.putInt("order",i-1)
                    templateFragment.arguments = args
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.add(R.id.ll_Fvertical,templateFragment)
                    fragmentTransaction.commit()
                }
            }
            else
            Toast.makeText(context ,"没有试剂模板", Toast.LENGTH_SHORT).show()
        }

    }


}