package com.liad.droptask

import android.app.Application
import com.liad.droptask.di.DaggerAppComponent


class DropApplication : Application() {

    val appComponent = DaggerAppComponent.create()

}