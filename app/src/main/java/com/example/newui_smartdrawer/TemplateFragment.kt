package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.ReagentTemplate
import kotlinx.android.synthetic.main.fragment_template.*

class TemplateFragment : Fragment() {
    private var reagentTemplate: ReagentTemplate?=null
    private var dbManager: DBManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template, container, false)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context)
        if(arguments!=null)
        {
            addTemplateLine()
        }
    }

    fun addTemplateLine(){
        val order = arguments.getInt("order")
        val arrListReagentTemplate = dbManager?.reagentTemplate
        reagentTemplate = arrListReagentTemplate?.get(order)
        tv_Ftemplate_name.text=reagentTemplate?.reagentName
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
