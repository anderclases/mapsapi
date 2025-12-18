package com.example.mapsapi

import android.app.Application
import com.google.android.libraries.places.api.Places

class SecretsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Places.initialize(this, BuildConfig.GMP_KEY)
    }
}