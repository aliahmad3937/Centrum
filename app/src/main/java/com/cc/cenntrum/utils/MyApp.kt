package com.cc.cenntrum.utils

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.cc.cenntrum.models.IncentiveResponse
import com.cc.cenntrum.models.PackagesResponse
import com.cc.cenntrum.ui.fragments.HomeFragment

import com.kaopiz.kprogresshud.KProgressHUD
import dagger.hilt.android.HiltAndroidApp
import java.util.*

var application: Context? = null

@HiltAndroidApp
class MyApp : Application() {




    lateinit var apiKey: String


    // lateinit var repositry: MainRepository
    companion object {
        const val url: String = "https://wh717090.ispot.cc/fitness/public/storage/"
        var isBottomNavShow = true
        var isMapVisit = false

        var selectedOption = 0
        var incentiveList: ArrayList<IncentiveResponse.Data>? = null
        var coinsList: ArrayList<PackagesResponse.Data>? = null
        var subscripList: ArrayList<PackagesResponse.Data>? = null
        lateinit var hud: KProgressHUD

        @JvmStatic
        lateinit var homeFragment: HomeFragment

        @JvmStatic
        lateinit var locationManager: LocationManager


        @JvmStatic
        fun hudProgress(context: Context) {
            hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
            hud.show()
        }


    }

    override fun onCreate() {
        super.onCreate()
        application = applicationContext
        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = this.packageManager
            .getApplicationInfo(
                applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        apiKey = ai.metaData["com.google.android.geo.API_KEY"].toString()

    }



}
