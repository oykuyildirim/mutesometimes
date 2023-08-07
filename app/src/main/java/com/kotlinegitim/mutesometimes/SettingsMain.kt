package com.kotlinegitim.mutesometimes

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kotlinegitim.mutesometimes.services.LocationService
import com.kotlinegitim.mutesometimes.services.TimeService
import com.kotlinegitim.mutesometimes.translater.AppLanguageTrans


class SettingsMain : AppCompatActivity() {

    lateinit var muteDetection : Switch
    lateinit var locDetection : Switch


    lateinit var spinner : Spinner

    companion object{

        var isClosed = false
        var isClosed2 = false


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main)


        muteDetection = findViewById(R.id.muteDetection)
        locDetection = findViewById(R.id.locDetection)
        spinner = findViewById(R.id.setLang)



        val prefences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val editor = prefences.edit()



        var isLocal =  prefences.getBoolean("location",true)
        var isTime =prefences.getBoolean("time", true)



        locDetection.setChecked(isLocal)


        locDetection.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked ) {


                Thread{

                    if(Build.VERSION.SDK_INT < 26){

                        startService(Intent(this, LocationService::class.java))
                    }

                    else {
                        startForegroundService(Intent(this, LocationService::class.java))

                    }

                    Handler(Looper.getMainLooper()).postDelayed({

                        locDetection.isClickable = true

                    },1500)

                }.start()



                locDetection.isClickable = false
                editor.putBoolean("location",isChecked)
                editor.apply()

                isClosed2 = false




            } else {


                Thread {
                    val myService = Intent(this, LocationService::class.java)
                    stopService(myService)

                    editor.putBoolean("location", isChecked)
                    editor.apply()



                    Handler(Looper.getMainLooper()).postDelayed({
                        locDetection.isClickable = true
                    }, 1500)

                }.start()


                locDetection.isClickable = false

                isClosed2 = true


            }

        })


        muteDetection.setChecked(isTime)

        muteDetection.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {



                Thread{

                    if(Build.VERSION.SDK_INT < 26){

                        startService(Intent(this, TimeService::class.java))
                    }

                    else {
                        startForegroundService(Intent(this, TimeService::class.java))

                    }


                    Handler(Looper.getMainLooper()).postDelayed({
                        muteDetection.isClickable = true
                    }, 1500)



                }.start()


                editor.putBoolean("time",isChecked)
                editor.apply()

                muteDetection.isClickable = false


                isClosed = false

            } else {

                Thread {
                    val myService = Intent(this, TimeService::class.java)
                    stopService(myService)

                    Handler(Looper.getMainLooper()).postDelayed({
                        muteDetection.isClickable = true
                    }, 1500)

                    isClosed = true
                }.start()


                muteDetection.isClickable = false
                editor.putBoolean("time",isChecked)
                editor.apply()

            }

        })



        val languages = resources.getStringArray(R.array.Languages)

        if (spinner != null) {
            val adapter = SpinnerCustomAdaptor(
                this,
               languages

            )
            spinner.adapter = adapter

            val sh_lang = prefences.getString("language","ENGLISH")
            val selectionPosition = adapter.getPosition(sh_lang)
            spinner.setSelection(selectionPosition)


            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    var language = languages[position]
                    editor.putString("language",language)
                    editor.apply()
                    val code = enumValueOf<Language>(language)


                    AppLanguageTrans().changeLanguange(this@SettingsMain, code.languange)

                    muteDetection.setText(getString(R.string.mute_time_dec))
                    locDetection.setText(getString(R.string.mute_loc_dec))
                    setTitle(getString(R.string.settings))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }


        }
    }

    override fun onBackPressed() {

        val intent = Intent(this@SettingsMain, MuteMainActivity::class.java)
        startActivity(intent)

        super.onBackPressed()
    }


}

