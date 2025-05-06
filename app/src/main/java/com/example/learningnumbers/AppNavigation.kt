package com.example.learningnumbers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = "main") {
        composable("main") {
            MainViewContent(
                navHostController,
                name = LocalContext.current.resources.getString(R.string.titleMainView),
                viewModel = mainViewModel
            )
        }
        composable("learning") {
            LearningView(
                navController = navHostController,
                numbers = mainViewModel.numbersToLearn(),
                locale = mainViewModel.selectedLanguage.value?.locale
                    ?: mainViewModel.defaultLanguage.locale
            )
        }
    }
}