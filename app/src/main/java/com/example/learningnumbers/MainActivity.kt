package com.example.learningnumbers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learningnumbers.ui.theme.LearningNumbersTheme

class MainActivity : ComponentActivity() {
    val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningNumbersTheme {
                AppNavigation(viewModel)
            }
        }
    }
}
