package com.kotlinegitim.mutesometimes

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kotlinegitim.mutesometimes.databinding.ActivityMuteMainBinding
import com.kotlinegitim.mutesometimes.services.LocationService
import com.kotlinegitim.mutesometimes.services.TimeService
import java.util.*

class MuteMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMuteMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMuteMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_mute_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_timeset, R.id.navigation_locationset
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !notificationManager.isNotificationPolicyAccessGranted)  // M = API 23
        {
            val intent = Intent(
                Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
            )
            startActivity(intent)
        }





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.settings -> openSettings()


        }
        return super.onOptionsItemSelected(item)
    }

    fun openSettings(){


         var intent = Intent(this, SettingsMain::class.java)
         startActivity(intent)


    }

}