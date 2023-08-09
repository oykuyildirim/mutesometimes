package com.kotlinegitim.mutesometimes

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_CONTACTS
import android.app.appsearch.SetSchemaRequest.READ_CONTACTS
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kotlinegitim.mutesometimes.dbOperations.DBOperationsLoc
import com.kotlinegitim.mutesometimes.services.LocationService
import com.kotlinegitim.mutesometimes.services.TimeService
import com.kotlinegitim.mutesometimes.translater.AppLanguageTrans

class SplashActivity : AppCompatActivity() {

    lateinit var  pText : TextView
    lateinit var  progressBar: ProgressBar
    lateinit var slogan : TextView

    companion object {
        var enableLoc: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)


        slogan = findViewById(R.id.slogan)

        val prefences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val openedTime = prefences.getBoolean("time",false)
        val openedLocation = prefences.getBoolean("location",false)


        val sh_lang = prefences.getString("language","ENGLISH")
        val code = enumValueOf<Language>(sh_lang.toString())
        AppLanguageTrans().changeLanguange(this, code.languange)


        LocationRequest()

        slogan.setText(getString(R.string.slogan))


        Thread{

            registerReceiver(BootRecevier(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))

        }.start()


        if(enableLoc == false){


        }


        if (BootRecevier.boot == true){
        }

        else{
            if (openedTime== true){
                Thread{


                    if(Build.VERSION.SDK_INT < 26){

                        startService(Intent(this, TimeService::class.java))
                    }

                    else {
                        startForegroundService(Intent(this, TimeService::class.java))

                    }

                }.start()

            }
            else{

                Thread{
                    stopService(Intent(this, TimeService::class.java))

                }.start()
            }
            if (openedLocation == true && enableLoc == true){
                Thread{
                    if(Build.VERSION.SDK_INT < 26){

                        startService(Intent(this, LocationService::class.java))
                    }

                    else {
                        startForegroundService(Intent(this, LocationService::class.java))

                    }

                }.start()
            }

            else{

                Thread {
                    stopService(Intent(this, LocationService::class.java))
                }.start()
            }


        }


        android.os.Handler().postDelayed({
            var intent = Intent(this, MuteMainActivity::class.java)
            startActivity(intent)
        }, 1500)





    }


    fun LocationRequest(){


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_FINE_LOCATION)) {



            } else {



                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            }
        } else {


            enableLoc = true

        }
    }




}