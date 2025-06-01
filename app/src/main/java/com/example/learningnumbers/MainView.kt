package com.example.learningnumbers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningnumbers.ui.theme.BackGroundGradientEnd
import com.example.learningnumbers.ui.theme.BackGroundGradientStart
import com.example.learningnumbers.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.app_name))
                    }},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(BackGroundGradientStart, BackGroundGradientEnd))
                )
                .padding(paddingValues)
                .padding(16.dp), // Additional padding for content
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val largeSpacerSize = 48.dp // Or even define these at a higher scope if used across multiple screens
            val mediumSpacerSize = 24.dp
            val btnHeight = 48.dp
            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(160.dp)
            )
            Spacer(modifier = Modifier.height(largeSpacerSize))
            Button(
                onClick = { navController.navigate(AppDestinations.CONFIG_LEARNING_NUMBERS_VIEW_ROUTE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(btnHeight)
            ) {
                Text("Go to Learning numbers", fontSize = Typography.bodyLarge.fontSize)
            }
            Spacer(modifier = Modifier.height(mediumSpacerSize))
            Button(
                onClick = { navController.navigate(AppDestinations.CONFIG_LEARNING_DATES_VIEW_ROUTE) },
                modifier = Modifier
                    .fillMaxWidth()

                    .height(btnHeight)
            ) {
                Text("Go to Learning dates", fontSize = Typography.bodyLarge.fontSize)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    MainView(navController = NavController(LocalContext.current))
}