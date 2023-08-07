package com.kotlinegitim.mutesometimes.services

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.kotlinegitim.mutesometimes.database.LocationDatabase
import com.kotlinegitim.mutesometimes.SettingsMain
import com.kotlinegitim.mutesometimes.dbOperations.DBOperationsLoc


class LocationService:Service(), LocationListener {
    var mHandler = Handler()



    var location_cur: String =""
    companion object{

        var long_comp:Double = 0.0
        var lat_comp : Double = 0.0
        var obj2 = mutableListOf<com.kotlinegitim.mutesometimes.models.Location>()
        var added_loc = false
        var entered = false

    }


    private lateinit var locationManager: LocationManager

    private val locationPermissionCode = 2
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)

        val db = Room.databaseBuilder(
            applicationContext,
            LocationDatabase::class.java,
            "LocationDatabase"
        ).build()

        if (added_loc == false) {

            val run = Runnable {

               obj2 = db.LocationDao().allLocation() as MutableList

            }
            Thread(run).start()
            added_loc = true

        }

        getLocation()





        if(obj2.count()!=0) {

            for(loc in obj2) {

                if (loc.location == location_cur && loc.active == true) {


                    if (entered == false) {

                        Handler().postDelayed({
                            Toast.makeText(this, "Sessize alındı", Toast.LENGTH_SHORT).show()
                        }, 1000)

                        println("bşjeke")

                        var notificationManager =
                            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && !notificationManager.isNotificationPolicyAccessGranted
                        )

                            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                        else {

                            val am: AudioManager =
                                baseContext.getSystemService(AUDIO_SERVICE) as AudioManager

                            am.ringerMode = AudioManager.RINGER_MODE_SILENT
                        }

                        entered = true
                    }

                    if (entered == true &&  loc.location == location_cur){

                        break

                    }

                    else{

                        entered = false
                    }

                }



            }

        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        if (SettingsMain.isClosed2 == false) {
            val restartServiceIntent = Intent(applicationContext, this.javaClass)
            restartServiceIntent.setPackage(packageName)
            startService(restartServiceIntent)
        }


        super.onTaskRemoved(rootIntent)


    }


    override fun onDestroy() {

        stopService(Intent(this, LocationService::class.java))
        println("canceled")

    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                Activity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)}
        override fun onLocationChanged(location: Location) {
            val lat = location.latitude
            val long = location.longitude

            lat_comp = lat
            long_comp = long

            location_cur =lat.toString()+","+long.toString()
            println(lat)
            println(long)
        }



}