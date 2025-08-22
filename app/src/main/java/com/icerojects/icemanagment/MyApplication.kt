package com.icerojects.icemanagment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Ese archivo inicializa Hilt en la aplicación y crea el contenedor de dependencias global.
@HiltAndroidApp
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }

}