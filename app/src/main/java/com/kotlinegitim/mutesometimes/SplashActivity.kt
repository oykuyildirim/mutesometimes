package com.kotlinegitim.mutesometimes

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.kotlinegitim.mutesometimes.services.LocationService
import com.kotlinegitim.mutesometimes.services.TimeService
import com.kotlinegitim.mutesometimes.translater.AppLanguageTrans

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var  pText : TextView
        lateinit var  progressBar: ProgressBar
        lateinit var slogan : TextView

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)

       // pText = findViewById(R.id.pText)
        slogan = findViewById(R.id.slogan)

        val prefences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val openedTime = prefences.getBoolean("time",true)
        val openedLocation = prefences.getBoolean("location",true)

        
        val sh_lang = prefences.getString("language","ENGLISH")
        val code = enumValueOf<Language>(sh_lang.toString())
        AppLanguageTrans().changeLanguange(this, code.languange)



        slogan.setText(getString(R.string.slogan))


        Thread{

            registerReceiver(BootRecevier(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))

        }.start()


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

            if (openedLocation == true){
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




            var intent = Intent(this, MuteMainActivity::class.java)
            startActivity(intent)







    }


}