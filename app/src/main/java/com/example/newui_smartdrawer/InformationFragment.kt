package com.example.newui_smartdrawer


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.R.drawable.image_18_f97251
import com.example.newui_smartdrawer.util.DBManager
import kotlinx.android.synthetic.main.fragment_information.*
import org.greenrobot.eventbus.EventBus

/**
 * A simple [Fragment] subclass.
 *
 */
class InformationFragment : Fragment() {
    private var dbManager:DBManager?=null
    private var scApp:SCApp?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager= DBManager(context.applicationContext)
        scApp= context.applicationContext as SCApp
        if (arguments != null) {
            if (arguments.getString("state") == "in") {
                var reagent =  dbManager?.getReagentByPos(scApp?.touchdrawer.toString(),scApp?.touchtable.toString(),scApp?.boxId.toString())
                tv_Finformation_use.visibility = View.GONE

                tv_Finforation_name.text=reagent?.reagentName
                tv_Finformation_manufactor.text=reagent?.reagentCreater
                tv_Finformation_data.text=reagent?.reagentInvalidDate
                tv_Finformation_id.text=reagent?.reagentId
                tv_Finformation_purity.text=reagent?.reagentPurity
                tv_Finformation_user.text=reagent?.reagentUser
                tv_Finformation_residue.text=reagent?.reagentSize+"ml"
            }
            if (arguments.getString("operation") == "operation") {
                ib_Finformation_jump.visibility = View.GONE
                cl_Finformation.setBackgroundColor(Color.TRANSPARENT)
            }
            if(arguments.getString("showMessage")=="show")
            {
                ib_Finformation_jump.visibility = View.GONE
                cl_Finformation.setBackgroundColor(Color.TRANSPARENT)
                var reagent =  dbManager?.getReagentByPos(arguments.getString("tablenum"),arguments.getString("pos"),scApp?.boxId.toString())
                if(reagent?.status==1)
                {
//                    tv_Finformation_use.visibility=View.GONE
                    tv_Finformation_use.text="在位"
                    tv_Finformation_use.setBackgroundColor(0x7f0600ae)
                }
                tv_Finforation_name.text=reagent?.reagentName
                tv_Finformation_manufactor.text=reagent?.reagentCreater
                tv_Finformation_data.text=reagent?.reagentInvalidDate
                tv_Finformation_id.text=reagent?.reagentId
                tv_Finformation_purity.text=reagent?.reagentPurity
                tv_Finformation_user.text=reagent?.reagentUser
                tv_Finformation_residue.text=reagent?.reagentSize+"ml"


            }
            if(arguments.getString("addreagent")=="addreagent")
            {
                var reagentId = arguments.getString("reagentID")
                var searchreagent = dbManager?.getReagentById(reagentId)
                if(searchreagent?.status==1)
                {
//                    tv_Finformation_use.visibility=View.GONE
                    tv_Finformation_use.text="在位"
                    tv_Finformation_use.setBackgroundColor(0x7f0600ae)

                }

                tv_Finforation_name.text=searchreagent?.reagentName
                tv_Finformation_manufactor.text=searchreagent?.reagentCreater
                tv_Finformation_data.text=searchreagent?.reagentInvalidDate
                tv_Finformation_id.text=searchreagent?.reagentId
                tv_Finformation_purity.text=searchreagent?.reagentPurity
                tv_Finformation_user.text=searchreagent?.reagentUser
                tv_Finformation_residue.text=searchreagent?.reagentSize+"ml"


            }

        }
        ib_Finformation_jump.setOnClickListener {
            var reagent =  dbManager?.getReagentById(tv_Finformation_id.text.toString())
            scApp?.touchdrawer=reagent!!.drawerId.toInt()
            scApp?.touchtable=reagent.reagentPosition.toInt()
            scApp?.boxId = reagent.reagentCabinetId.toInt()
            var eventMessenge = SerachEvent()
            eventMessenge.setMsg("update")
            EventBus.getDefault().postSticky(eventMessenge)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setRetainInstance(true)
        super.onCreate(savedInstanceState)

    }


}
