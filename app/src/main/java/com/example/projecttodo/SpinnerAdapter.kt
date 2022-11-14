package com.example.projecttodo

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class SpinnerAdapter(var context: Context, var
data: List<Tags>): BaseAdapter() {

    var inflter: LayoutInflater
    init {
        inflter = LayoutInflater.from(context)
    }
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {

        val view = inflter.inflate(R.layout.spinner_item,null)
        val spinnerItem = view.findViewById<View>(R.id.spinnerItem) as TextView

        spinnerItem.text = data[i].tag
        return view
    }

}