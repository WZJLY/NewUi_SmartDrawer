package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager

class HorizontalFragment : Fragment() {
    private var dbManager:DBManager?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horizontal, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        val arrlistBox = dbManager?.boxes
        val sum = arrlistBox!!.size
        if(sum > 0)
        {
            for (i in 1..sum){
                    val fragment = childFragmentManager.beginTransaction()
                    val cabinetFragment = CabinetFragment()
                    val args = Bundle()
                    args.putInt("boxID", i)
                    cabinetFragment.arguments = args
                    fragment.add(R.id.ll_Fhorizontal, cabinetFragment)
                    fragment.commit()
            }
        }
        else
        {
            Toast.makeText(context, "请添加柜子", Toast.LENGTH_SHORT).show()
        }

    }


}
