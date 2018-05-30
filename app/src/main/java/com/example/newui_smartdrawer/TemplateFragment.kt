package com.example.newui_smartdrawer


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.ReagentTemplate
import kotlinx.android.synthetic.main.fragment_template.*

class TemplateFragment : Fragment() {
    private var reagentTemplate: ReagentTemplate?=null
    private var dbManager: DBManager? = null
    var activityCallback:TemplateFragment.deletTemplatelisten? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template, container, false)

    }

    interface deletTemplatelisten {
        fun deletTemplateClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as deletTemplatelisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }

    }
    private fun delettemplateClicked(text: String) {
        activityCallback?.deletTemplateClick(text)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        if(arguments!=null)
        {
            addTemplateLine()
        }
        ib_Ftemplate_del.setOnClickListener({
            //单条删除试剂
            var unit:String? = null
            var purity:String? = null
            if(tv_Ftemplate_state.text.toString()=="固体") {
                unit = "g"
            }
            else
                unit="ml"
            purity = tv_Ftemplate_purity.text.toString().substring(0,tv_Ftemplate_purity.text.toString().length-1)
            dbManager?.deletReagentTemplateByInfo("",tv_Ftemplate_name.text.toString(),tv_Ftemplate_anotherName.text.toString(),"",
                    "", purity,tv_Ftemplate_volume.text.toString(),tv_Ftemplate_manufactor.text.toString() ,
                    tv_Ftemplate_code.text.toString(),unit,tv_Ftemplate_density.text.toString())
            delettemplateClicked("delet_template")


        })
    }

    fun addTemplateLine(){
        val order = arguments.getInt("order")
        val arrListReagentTemplate = dbManager?.reagentTemplate
        reagentTemplate = arrListReagentTemplate?.get(order)
        tv_Ftemplate_name.text=reagentTemplate?.reagentName
        Log.d("wzj",reagentTemplate?.reagentAlias)
        tv_Ftemplate_anotherName.text=reagentTemplate?.reagentAlias
        tv_Ftemplate_volume.text=reagentTemplate?.reagentSize
        tv_Ftemplate_code.text=reagentTemplate?.reagentGoodsID
        tv_Ftemplate_manufactor.text=reagentTemplate?.reagentCreater
        tv_Ftemplate_purity.text=reagentTemplate?.reagentPurity+"%"
        tv_Ftemplate_density.text=reagentTemplate?.reagentDensity
        if(reagentTemplate?.reagentUnit=="g")
            tv_Ftemplate_state.text="固体"
        else
            tv_Ftemplate_state.text="液体"
    }

}
