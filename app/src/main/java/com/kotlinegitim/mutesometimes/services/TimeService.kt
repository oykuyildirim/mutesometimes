package com.kotlinegitim.mutesometimes.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.widget.Toast
import androidx.room.Room
import com.kotlinegitim.mutesometimes.database.ClockDatabase
import com.kotlinegitim.mutesometimes.SettingsMain
import com.kotlinegitim.mutesometimes.models.MuteClock
import com.kotlinegitim.mutesometimes.ui.TimeSet.TimeSetFragment
import java.text.SimpleDateFormat
import java.util.*


class TimeService : Service(){


    var mHandler = Handler()

    companion object{
        var added = false
        var entered_time = false
        var checked = false
        var obj2 = mutableListOf<MuteClock>()
    }

    var bool : Boolean = false
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)



        val timeNow = getcurrentTime()

        val msg: Message = mHandler.obtainMessage()
        msg.arg1 = startId
        mHandler.sendMessage(msg)

        TimeSetFragment.muteList



        val db = Room.databaseBuilder(
           applicationContext,
            ClockDatabase::class.java,
            "AlarmDatabase"
        ).build()


        if (added == false) {

            val run = Runnable {

                obj2 = db.MuteClockDao().allClocks() as MutableList

            }
            Thread(run).start()
            added = true

        }


        if(obj2.count()!=0) {

            for(clock in obj2) {


                if (clock.clock == timeNow && clock.active == true) {


                    if (entered_time == false){
                        Handler().postDelayed({
                            Toast.makeText(this, "Sessize alındı", Toast.LENGTH_SHORT).show()
                        }, 1000)


                        var notificationManager =
                            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && !notificationManager.isNotificationPolicyAccessGranted
                        )

                            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                        else {

                            val am: AudioManager = baseContext.getSystemService(AUDIO_SERVICE) as AudioManager

                            am.ringerMode = AudioManager.RINGER_MODE_SILENT
                        }

                        entered_time = true
                    }

                    if (entered_time == true &&  clock.clock == timeNow){

                        break

                    }

                    else{

                        entered_time = false
                    }


                }

            }

        }


        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        if (SettingsMain.isClosed == false) {
            val restartServiceIntent = Intent(applicationContext, this.javaClass)
            restartServiceIntent.setPackage(packageName)
            startService(restartServiceIntent)
        }


        super.onTaskRemoved(rootIntent)


    }

    fun getcurrentTime() : String{

        val sdf = SimpleDateFormat("HH:mm:ss")
        var currentDateAndTime = sdf.format(Date())

        currentDateAndTime = currentDateAndTime.dropLast(2)+"00"

        return currentDateAndTime
    }

    override fun onDestroy() {

        stopService(Intent(this, TimeService::class.java))
        println("canceled")

    }


}





