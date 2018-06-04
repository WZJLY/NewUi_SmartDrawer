package com.example.newui_smartdrawer


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.fragment_information.*

/**
 * A simple [Fragment] subclass.
 *
 */
class InformationFragment : Fragment() {
    private var dbManager:DBManager?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbManager= DBManager(context.applicationContext)
        if (arguments != null) {
            if (arguments.getString("state") == "in") {
                tv_Finformation_use.visibility = View.GONE
            }
            if (arguments.getString("operation") == "operation") {
                ib_Finformation_jump.visibility = View.GONE
                cl_Finformation.setBackgroundColor(Color.TRANSPARENT)
            }
            if(arguments.getString("showMessage")=="show")
            {
                ib_Finformation_jump.visibility = View.GONE
                cl_Finformation.setBackgroundColor(Color.TRANSPARENT)
                val reagent =  dbManager?.getReagentByPos(arguments.getString("tablenum"),arguments.getString("pos"))
                if(reagent?.status==1)
                {
                    tv_Finformation_use.visibility=View.GONE
                }
                tv_Finforation_name.text=reagent?.reagentName
                tv_Finformation_manufactor.text=reagent?.reagentCreater
                tv_Finformation_data.text=reagent?.reagentInvalidDate
                tv_Finformation_id.text=reagent?.reagentId
                tv_Finformation_purity.text=reagent?.reagentPurity
                tv_Finformation_user.text=reagent?.reagentUser
                tv_Finformation_residue.text=reagent?.reagentSize+"ml"


            }



        }
        ib_Finformation_jump.setOnClickListener {

        }
    }
}
