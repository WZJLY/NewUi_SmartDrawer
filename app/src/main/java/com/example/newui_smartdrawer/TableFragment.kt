package com.example.newui_smartdrawer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import kotlinx.android.synthetic.main.fragment_table.*


class TableFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    fun addNum(num: Int){
        tl_Ftable.removeAllViews()
        val params = TableRow.LayoutParams(55, 55)
        for(i in 1..num){
            val tableRow = TableRow(context)
            for(j in 1..num){
                val button = Button(context)
                button.layoutParams = params
                button.isFocusable = false
                button.id = (i-1)*num+j
                button.textSize = 12F
                //按钮底色和字体颜色
                button.setBackgroundResource(R.drawable.btn_table_f6f6f6_style)
                button.setTextColor(context.resources.getColor(R.color.tb_text))

                button.setOnClickListener { view->
                    view.isFocusable = true
                    view.requestFocus()
                    view.requestFocusFromTouch()
                    var row = button.id.toString()
                }
                tableRow.addView(button)
            }
            tl_Ftable.addView(tableRow)
        }
    }
}
