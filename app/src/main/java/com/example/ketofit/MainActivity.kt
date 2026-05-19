package com.example.ketofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ketofit.navigation.AppNavGraph
import com.example.ketofit.ui.theme.KetoFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as KetoFitApplication

        setContent {
            KetoFitTheme {
                AppNavGraph(container = app.container)
            }
        }
    }
}

