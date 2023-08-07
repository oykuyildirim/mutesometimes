package com.kotlinegitim.mutesometimes

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import androidx.room.Room
import com.kotlinegitim.mutesometimes.services.LocationService
import com.kotlinegitim.mutesometimes.services.TimeService

class BootRecevier: BroadcastReceiver(){


    companion object{

        var boot = false
    }

    override fun onReceive(context: Context?, intent: Intent?) {


        val prefences = context!!.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val openedTime = prefences.getBoolean("time",true)
        val openedLocation = prefences.getBoolean("location",true)


        println("boot")
        boot = true


        if (openedTime== true){
            Thread{


                if(Build.VERSION.SDK_INT < 26){

                    context!!.startService(Intent(context, TimeService::class.java))
                }

                else {
                    context!!.startForegroundService(Intent(context, TimeService::class.java))

                }

            }.start()

        }
        else{

            Thread{
                context!!.stopService(Intent(context, TimeService::class.java))

            }.start()
        }

        if (openedLocation == true){
            Thread{
                if(Build.VERSION.SDK_INT < 26){

                    context!!.startService(Intent(context, LocationService::class.java))
                }

                else {
                    context!!.startForegroundService(Intent(context, LocationService::class.java))

                }

            }.start()
        }

        else{

            Thread {
                context!!.stopService(Intent(context, LocationService::class.java))
            }.start()
        }




    }

}