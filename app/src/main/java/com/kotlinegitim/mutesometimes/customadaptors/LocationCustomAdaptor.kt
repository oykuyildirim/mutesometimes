package com.kotlinegitim.mutesometimes.customadaptors

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.room.Room
import com.kotlinegitim.mutesometimes.R
import com.kotlinegitim.mutesometimes.database.LocationDatabase
import com.kotlinegitim.mutesometimes.dbOperations.DBOperationsLoc
import com.kotlinegitim.mutesometimes.models.Location
import com.kotlinegitim.mutesometimes.services.LocationService


class LocationCustomAdaptor(val context: Activity, val resource: Int, val objects: MutableList<Location>) :
    ArrayAdapter<Location>(context, resource, objects) {

    lateinit var location : TextView

    lateinit var switch: Switch

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val root = context.layoutInflater.inflate(resource,null,true)

        val db2 = Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            "LocationDatabase"
        ).build()
        switch = root.findViewById(R.id.switch1)
        location = root.findViewById(R.id.clockText)

        var loc = objects.get(position)

        if (loc.title.isNullOrEmpty()){

            switch.text = context.getString(R.string.noTitle)
        }
        else {
            switch.text = loc.title
        }

        switch.setChecked(loc.active)

        println( switch.isChecked)
        location.text = loc.location

        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {


                LocationService.entered = false
                android.os.Handler().postDelayed({
                    if (root.parent != null) {
                        DBOperationsLoc().allLoc(root.parent as ListView, context, db2)
                    }
                }, 1500)

                DBOperationsLoc().update(loc,true,db2)

                switch.isClickable = false
            }

            else{



                android.os.Handler().postDelayed({
                   if (root.parent!= null) {
                       DBOperationsLoc().allLoc(root.parent as ListView, context, db2)
                   }
                }, 1500)

                switch.isClickable = false
                DBOperationsLoc().update(loc,false,db2)



            }
        })

        root.setOnLongClickListener {


            deleteAlert(root.parent as ListView, loc,db2)


            true

        }

        return root
    }

    fun ActivateAlert( mute: Location, db : LocationDatabase){

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Delete")

        builder.setMessage("Do you want to activate ?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->


            DBOperationsLoc().update(mute,true,db)
        }

        builder.setNegativeButton("No"){dialogInterface, which ->


            DBOperationsLoc().update(mute,false,db)
        }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun deleteAlert(listView: ListView, mute: Location, db : LocationDatabase){

        val builder = AlertDialog.Builder(context)

        builder.setTitle(context.getString(R.string.delete))

        builder.setMessage(context.getString(R.string.sure))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(context.getString(R.string.Yes)){dialogInterface, which ->


            android.os.Handler().postDelayed({
                DBOperationsLoc().allLoc(listView, context, db)
            }, 1500)

            DBOperationsLoc().delete(mute,db)


        }

        builder.setNegativeButton(context.getString(R.string.No)){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }


}