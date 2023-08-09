package com.kotlinegitim.mutesometimes.dbOperations

import android.app.Activity
import android.provider.Settings.Global.getString
import android.widget.ListView
import android.widget.Toast
import com.kotlinegitim.mutesometimes.customadaptors.LocationCustomAdaptor
import com.kotlinegitim.mutesometimes.R
import com.kotlinegitim.mutesometimes.database.LocationDatabase
import com.kotlinegitim.mutesometimes.models.Location
import com.kotlinegitim.mutesometimes.services.LocationService

class DBOperationsLoc {

    fun addToDatabase(db2: LocationDatabase, loc : Location, act:Activity){



        val run = Runnable {

            try {
                db2.LocationDao().InsertLoc(loc)
                LocationService.added_loc = false

            }
            catch ( e : Exception){


                act.runOnUiThread {
                    println(e.message.toString())
                    Toast.makeText(act,act.getString(R.string.sameloc), Toast.LENGTH_SHORT).show()
                }


            }

        }
        Thread(run).start()
        Thread()



    }


    fun allLoc(LocationList: ListView, act: Activity, db : LocationDatabase) {




        val run = Runnable {

            var list = db.LocationDao().allLocation() as MutableList

            act.runOnUiThread {

                val adapter = LocationCustomAdaptor(act, R.layout.list_clock_custom_layout, list)
                LocationList.adapter = adapter
                adapter.notifyDataSetChanged()

            }


        }
        Thread(run).start()
        Thread(run).join()
    }

    fun delete(loc: Location, db : LocationDatabase){



        val run = Runnable {

            var alarm = Location(loc.id,
               loc.location,
                loc.title,
                loc.active
            )
            db.LocationDao().DeleteLoc(alarm)

           LocationService.added_loc= false

        }

        Thread(run).start()
        Thread(run).join()


    }
    fun update(mute: Location, active:Boolean, db : LocationDatabase){

        println("update")

        val run = Runnable {


            println("thread")
            var alarm = Location(mute.id,
                mute.location,
                mute.title,
                active
            )
            db.LocationDao().UpdateLoc(alarm)

            LocationService.added_loc = false

        }

        Thread(run).start()
        Thread(run).join()


    }
}