package com.anangkur.synrgychapter6

import android.app.Application
import com.anangkur.synrgychapter6.di.Provider

class Application : Application() {

    lateinit var provider: Provider

    override fun onCreate() {
        super.onCreate()

        provider = Provider(applicationContext)
    }
}