package com.kotlinegitim.mutesometimes.translater

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

class AppLanguageTrans {
    fun changeLanguange(act : Activity, lang:String){

        val locale = Locale(lang)
        Locale.setDefault(locale)
        var resources: Resources = act.getResources()
        var config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())

    }
}