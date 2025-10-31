package com.example.ositopolarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme
import com.example.ositopolarapp.features.`client-module`.ui.screen.MainScreenWithDrawer

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OsitoPolarAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenWithDrawer()
                }
            }
        }
    }
}