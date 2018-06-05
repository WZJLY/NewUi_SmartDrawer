package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.Reagent
import com.example.newui_smartdrawer.util.ReagentUserRecord
import kotlinx.android.synthetic.main.fragment_record.*

class RecordFragment : Fragment() {
    private var dbManager:DBManager?=null
    private var date:String?=null
    private var reagentUserRecord: ReagentUserRecord? = null
    private var reagentName:String?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        if(arguments!=null)
        {
            date = arguments.getString("date")
            reagentUserRecord=dbManager?.getReagentUseRecordByDate(date)
            tv_Frecord_id.text=reagentUserRecord?.reagentId
//            reagentName=dbManager!!.getReagentById(reagentUserRecord?.reagentId).reagentName
            when(reagentUserRecord?.operationType){
                1 ->{
                    tv_Frecord_type.text = "入柜"
                }
                2->{
                    tv_Frecord_type.text = "取用"
                }
                3->{
                    tv_Frecord_type.text = "归还"
                }
                4->{
                    tv_Frecord_type.text = "移除"
                }
            }
            tv_Frecord_date.text=reagentUserRecord?.operationTime
            tv_Frecord_user.text=reagentUserRecord?.operator
            tv_Frecord_weight.text=reagentUserRecord?.reagentTotalSize
            tv_Frecord_allowance.text=reagentUserRecord?.reagentSize
            tv_Frecord_wastage.text=reagentUserRecord?.consumption
        }

    }

}
