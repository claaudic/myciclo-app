package com.example.myciclo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.myciclo.navigation.Navigation
import com.example.myciclo.ui.theme.MyCicloTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MyCicloTheme {

                // Las rutas y pantallas están centralizadas en navigation/Navigation.kt
                val navController = rememberNavController()

                Navigation(navController = navController)
            }
        }
    }
}
