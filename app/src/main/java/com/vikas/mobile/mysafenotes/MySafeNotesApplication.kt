package com.vikas.mobile.mysafenotes

import android.app.Application
import com.vikas.mobile.mysafenotes.data.MySafeNotesDatabase

class MySafeNotesApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        InstanceFactory.instantiate(MySafeNotesDatabase.getInstance(applicationContext))
    }
}