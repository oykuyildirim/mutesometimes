package com.kotlinegitim.mutesometimes

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class SpinnerCustomAdaptor(private val context: Activity, private val list: Array<String>) :
    ArrayAdapter<String>(context,
    R.layout.spinner_custom_layout,list)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {




        return DropdownView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {



        return DropdownView(position,convertView,parent)
    }


    fun DropdownView (position: Int, convertView: View?, parent: ViewGroup) : View{
        val dropdownLayout = context.layoutInflater.inflate(R.layout.spinner_custom_layout,null,true)



        val Image = dropdownLayout.findViewById<ImageView>(R.id.flag)
        val text = dropdownLayout.findViewById<TextView>(R.id.language)

        val obj = enumValueOf<Language>(list[position])

        Image.setImageResource(obj.flag)
        text.text = list[position]

        return dropdownLayout

    }



}