package com.example.learningnumbers

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.learningnumbers.ui.theme.BackGroundGradientEnd
import com.example.learningnumbers.ui.theme.BackGroundGradientStart
import com.example.learningnumbers.ui.theme.Purple40
import com.example.learningnumbers.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigLearningDatesView(navController: NavController, viewModel: ViewModelDates) {
    val languages = viewModel.availableLanguages()

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = LocalContext.current.resources.getString(R.string.titleConfigLearningDatesView))},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }){
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(BackGroundGradientStart, BackGroundGradientEnd))
                )
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val spacer = 10.dp
            Spacer(modifier = Modifier.padding(spacer))
            LanguageSelector(
                languages = languages,
                language = viewModel.selectedLanguage,
                onSelectedLanguage = { viewModel.selectLanguage(it) })
            Spacer(modifier = Modifier.padding(spacer))
            // TODO:: Range years, months, days
//            NumbersRangeSelector(
//                numbersRange = viewModel.numbersRanges,
//                selectedNumbersRange = viewModel.selectedNumbersRange,
//                onNumbersRange = { viewModel.updatedNumbersRange(it) })
            Spacer(modifier = Modifier.padding(spacer))
            // TODO:: Randomize date properties
//            CheckboxWithLabel(onRandomize = {
//                viewModelupdateRandomizeNumbers(it)
//            })
            Spacer(modifier = Modifier.padding(spacer * 4))
            Button(
                onClick = {
                    navController.navigate(AppDestinations.LISTENING_NUMBERS_VIEW_ROUTE)
                },
                shape = RoundedCornerShape(size = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Start learning", fontSize = Typography.bodyLarge.fontSize)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConfigLearningDatesViewPreview() {
    val navController = rememberNavController()
    ConfigLearningDatesView(navController = navController, viewModel = ViewModelDates())
}