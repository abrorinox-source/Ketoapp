package com.example.ketofit

import android.app.Application

class KetoFitApplication : Application() {
    val container by lazy { AppContainer(this) }
}

