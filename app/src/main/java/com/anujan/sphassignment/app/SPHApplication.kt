package com.anujan.sphassignment.app

import android.app.Application
import com.anujan.sphassignment.di.AppComponent
import com.anujan.sphassignment.di.AppModule
import com.anujan.sphassignment.di.DaggerAppComponent

class SPHApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}