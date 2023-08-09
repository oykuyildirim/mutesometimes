package com.kotlinegitim.mutesometimes.customadaptors

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Switch
import android.widget.TextView
import androidx.room.Room
import com.kotlinegitim.mutesometimes.R
import com.kotlinegitim.mutesometimes.database.ClockDatabase
import com.kotlinegitim.mutesometimes.dbOperations.DBOperationsTime
import com.kotlinegitim.mutesometimes.models.MuteClock
import com.kotlinegitim.mutesometimes.services.TimeService


class MuteClockCustomAdaptor(val context: Activity, val resource: Int, val objects: MutableList<MuteClock>) :
    ArrayAdapter<MuteClock>(context, resource, objects) {

    lateinit var switch: Switch
    lateinit var clock : TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val root = context.layoutInflater.inflate(resource,null,true)

        switch = root.findViewById(R.id.switch1)
        clock = root.findViewById(R.id.clockText)

        val mute = objects.get(position)


        val db = Room.databaseBuilder(
            context,
            ClockDatabase::class.java,
            "AlarmDatabase"
        ).build()

        clock.text= mute.clock

        if (mute.title.isNullOrEmpty()){

            switch.text = context.getString(R.string.noTitle)
        }
        else {
            switch.text = mute.title
        }

        switch.setChecked(mute.active)

        switch.setOnCheckedChangeListener({ _ , isChecked ->

           println(switch.isClickable)
            if (isChecked) {


                TimeService.entered_time = false
                android.os.Handler().postDelayed({

                    if (root.parent != null) {
                        DBOperationsTime().allClock(root.parent as ListView, context, db)
                    }
                }, 1500)

                switch.isClickable = false
                DBOperationsTime().update(mute,true,db)


            }

            else{




                    android.os.Handler().postDelayed({

                        if(root.parent != null) {
                            DBOperationsTime().allClock(root.parent as ListView, context, db)
                        }
                    }, 1500)

                    switch.isClickable = false
                    DBOperationsTime().update(mute, false, db)


            }

        })

        root.setOnLongClickListener {


            deleteAlert(root.parent as ListView, mute,db)


            true

        }

        return root
    }


    fun deleteAlert(listView: ListView, mute: MuteClock, db : ClockDatabase){

        val builder = AlertDialog.Builder(context)

        builder.setTitle(context.getString(R.string.delete))

        builder.setMessage(context.getString(R.string.sure))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(context.getString(R.string.Yes)){dialogInterface, which ->


            android.os.Handler().postDelayed({
               DBOperationsTime().allClock(listView, context, db)
            }, 1500)

               DBOperationsTime().delete(mute,db)


        }

        builder.setNegativeButton(context.getString(R.string.No)){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    /*fun allClock(clockList: ListView,db: ClockDatabase) {



        val run = Runnable {

            var list = db.MuteClockDao().allClocks() as MutableList

            context.runOnUiThread {

                val adapter = MuteClockCustomAdaptor(context, R.layout.list_clock_custom_layout, list)
                clockList.adapter = adapter
                adapter.notifyDataSetChanged()

            }


        }
        Thread(run).start()
        Thread(run).join()
    }

    fun delete(mute:MuteClock,db: ClockDatabase){



        val run = Runnable {

            var alarm = MuteClock(mute.id,
                mute.clock,
                mute.title,
                mute.active
            )
            db.MuteClockDao().Delete(alarm)

            MuteService.added = false

        }

        Thread(run).start()
        Thread(run).join()


    }
    fun update(mute:MuteClock, active:Boolean,db: ClockDatabase){


        val run = Runnable {

            var alarm = MuteClock(mute.id,
                mute.clock,
                mute.title,
                active
            )
            db.MuteClockDao().Update(alarm)

            MuteService.added = false

        }

        Thread(run).start()
        Thread(run).join()


    }*/

}


