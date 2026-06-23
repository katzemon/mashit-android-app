package com.mashiverse.mashit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mashiverse.mashit.ui.screens.Main
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.ui.theme.MashitTheme
import com.reown.appkit.client.AppKit
import dagger.hilt.android.AndroidEntryPoint

@Suppress("UnstableApiUsage")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppKit.register(this)

        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()

            MashitTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                ) {
                    Main(navController = navController!!)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        try {
            navController?.handleDeepLink(intent)
        } catch (_: Exception) { }
    }
}