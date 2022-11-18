package com.example.projecttodo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MySpinnerAdapter(val context: Context, var data:List<Tags>):BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Tags {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.spinner_item,p2,false)

        val spinnerItem = view.findViewById<View>(R.id.spinnerItem) as TextView
        spinnerItem.text = getItem(p0).tag

        return view
    }

}
