package com.example.newui_smartdrawer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.Drawer
import kotlinx.android.synthetic.main.fragment_set_cabinet.*


class SetCabinetFragment : Fragment() {
    private var dbManager:DBManager?=null
    private var drawer:Drawer?=null
    private var activityCallback:SetCabinetFragment.addDrawerbuttonlisten? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_cabinet, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        updateDrawer()
        ib_FsetCabinet_addDrawer.setOnClickListener({
            addDrawerbuttonClicked("addDrawer")

        })
    }
    fun updateDrawer()
    {
        val arrListDrawers = dbManager?.getDrawers()
        val sum = arrListDrawers!!.size
        if(sum > 0)
        {
            for (i in 1..sum){
                val fragment = childFragmentManager.beginTransaction()
                val drawerFragment= DrawerFragment()
                val args = Bundle()
                args.putInt("drawerID", i)
                drawerFragment.arguments = args
                fragment.add(R.id.ll_FsetCabinet_drawer,drawerFragment)
                fragment.commit()
            }
        }
        else
        {
            Toast.makeText(context, "请添加抽屉", Toast.LENGTH_SHORT).show()
        }
    }

    interface addDrawerbuttonlisten {
        fun addDrawerButtonClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as  addDrawerbuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun addDrawerbuttonClicked(text: String) {
        activityCallback?.addDrawerButtonClick(text)
    }







}
