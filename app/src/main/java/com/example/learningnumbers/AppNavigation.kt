package com.example.learningnumbers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AppDestinations {
    const val MAIN_VIEW_ROUTE = "main"
    const val CONFIG_LEARNING_NUMBERS_VIEW_ROUTE = "config_learning_numbers"
    const val CONFIG_LEARNING_DATES_VIEW_ROUTE = "config_learning_dates"
    const val LISTENING_DATES_VIEW_ROUTE = "listening_dates"
    const val LISTENING_NUMBERS_VIEW_ROUTE = "listening_numbers"
}

@Composable
fun AppNavigation() {
    val viewModelNumbers = ViewModelNumbers()
    val viewModelDates = ViewModelDates()
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = AppDestinations.MAIN_VIEW_ROUTE) {
        composable(AppDestinations.MAIN_VIEW_ROUTE) {
            MainView(navHostController)
        }
        composable(AppDestinations.CONFIG_LEARNING_NUMBERS_VIEW_ROUTE) {
            ConfigLearningNumbersView(
                navHostController,
                name = LocalContext.current.resources.getString(R.string.titleConfigLearningNumbersView),
                viewModel = viewModelNumbers
            )
        }
        composable(AppDestinations.LISTENING_NUMBERS_VIEW_ROUTE) {
            ListeningNumbersView(
                navController = navHostController,
                numbers = viewModelNumbers.numbersToLearn(),
                locale = viewModelNumbers.defaultLanguage().locale
            )
        }
        composable(AppDestinations.CONFIG_LEARNING_DATES_VIEW_ROUTE) {
            ConfigLearningDatesView(navHostController,
                viewModel = viewModelDates)
        }
        composable(AppDestinations.LISTENING_DATES_VIEW_ROUTE) {
            ListeningDatesView(
                navController = navHostController,
                viewModel = viewModelDates
            )
        }
    }
}
