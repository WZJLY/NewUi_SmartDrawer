package com.example.newui_smartdrawer


import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.ReagentTemplate
import kotlinx.android.synthetic.main.fragment_into.*
import java.util.*

class IntoFragment : Fragment() {
    private var dbManager: DBManager? = null
    private var reagentTemplate: ReagentTemplate?=null
    private var scApp: SCApp? = null
    var activityCallback:IntoFragment.intobuttonlisten? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_into, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context.applicationContext)
        val value = arguments.getString("scan_value")
        val data_list = ArrayAdapter<String>(context, R.layout.information_spinner_style)
        val arrListReagentTemplate = dbManager?.reagentTemplate
        val sum = arrListReagentTemplate!!.size
        initDatePicker()
        if(sum == 0)
        {
            Toast.makeText(context.applicationContext, "当前没有试剂模板", Toast.LENGTH_SHORT).show()
        }
        else
        {
            if(sum>0) {
                for (i in 1..sum) {
                    reagentTemplate = arrListReagentTemplate[i - 1]
                    data_list.add(reagentTemplate!!.reagentName+","+"纯度："+reagentTemplate!!.reagentPurity+"%"
                            +","+reagentTemplate!!.reagentCreater+","+reagentTemplate!!.reagentSize+reagentTemplate!!.reagentUnit)
                }
            }
        }
        data_list.setDropDownViewResource(R.layout.information_dropdown_style)  //下拉框通过遍历试剂模板试剂库添加源
        sp_Finto_type.adapter=data_list
        sp_Finto_type.setSelection(0)
        sp_Finto_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        pos: Int, id: Long) {
                scApp?.templateNum= pos
                tv_Finto_residue.text = dbManager!!.reagentTemplate[pos].reagentUnit
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        if(value != null) {
            et_Finto_code.setText(value)
                et_Finto_load.isFocusable = true
                et_Finto_load.isFocusableInTouchMode = true
                et_Finto_load.requestFocus()
        }
        else{
            et_Finto_code.isFocusable = true
            et_Finto_code.isFocusableInTouchMode = true
            et_Finto_code.requestFocus()
        }
        btn_Finto_code.setOnClickListener{
            activityCallback?.intobuttononClick("scan")
        }
        btn_Finto_weight.setOnClickListener {
            activityCallback?.intobuttononClick("weight")
        }

        tv_Finto_data.setOnClickListener {
            val cal = Calendar.getInstance()
            val dialog = DatePickerDialog(context, null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确认", DialogInterface.OnClickListener { _, _ ->
                val datePicker = dialog.datePicker
                val year = datePicker.year
                val month = datePicker.month+1
                val day = datePicker.dayOfMonth
                tv_Finto_data.text = ""+year+"年"+month+"月"+day+"日"
            })
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", DialogInterface.OnClickListener { _, _ ->
                Log.d("setDate","取消")
            })
            dialog.window.setGravity(Gravity.CENTER)
            dialog.datePicker.calendarViewShown = false
            dialog.show()
        }
    }

    fun initDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)+1
        val month = cal.get(Calendar.MONTH)+1
        val day  = cal.get(Calendar.DAY_OF_MONTH)
        tv_Finto_data.text = ""+year+"年"+month+"月"+day+"日"
    }
    interface intobuttonlisten {
        fun intobuttononClick(text: String)
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as intobuttonlisten
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }
    }
}
