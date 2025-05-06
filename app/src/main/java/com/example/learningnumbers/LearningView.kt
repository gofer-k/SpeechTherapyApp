@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.learningnumbers

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learningnumbers.ui.theme.BackGroundGradientEnd
import com.example.learningnumbers.ui.theme.BackGroundGradientStart
import com.example.learningnumbers.ui.theme.Purple40
import java.util.Locale

@Composable
fun LearningView(navController: NavController, numbers: List<Int>, locale: Locale) {
    var topBarHeight by remember { mutableIntStateOf(0) }
    val textColor = Color.Black
    val textSize = 24.sp
    var currentNumberIndex by remember { mutableIntStateOf(0) }
    val currentNumber = numbers.elementAtOrNull(currentNumberIndex)
    val sortedNumbers = numbers.sorted()
    val textRange = IntRange(sortedNumbers.first(), sortedNumbers.last()).toString()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.onGloballyPositioned { topBarHeight = it.size.height },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Learning View",
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                })
        }) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(BackGroundGradientStart, BackGroundGradientEnd))
                )
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val ctx = LocalContext.current

            Text(
                modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
                text = textRange, color = textColor, fontSize = textSize
            )

            if (currentNumber != null && currentNumberIndex < numbers.count()) {
                ListenButton(
                    context = ctx,
                    currentNumber.toString(),
                    locale, 1.0f,
                    ttsViewModel = TtsViewModel(),
                    onListenDone = { suceess ->
                        if (suceess) {
                            currentNumberIndex++
                        }
                    })
            } else {
                Text(
                    modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
                    text = "No more numbers", color = textColor, fontSize = textSize
                )
            }
        }
    }
}

@Composable
fun ListenButton(
    context: Context,
    number: String,
    locale: Locale,
    speechRate: Float,
    ttsViewModel: TtsViewModel = TtsViewModel(),
    onListenDone: (Boolean) -> Unit
) {
    Button(
        modifier = Modifier
            .clip(CircleShape)
            .size(90.dp),
        onClick = {
            ttsViewModel.onListenTrainingPhrase(
                number, locale, speechRate, context,
                onFinishedSpeech = { success -> onListenDone(success) })
        }) {
        Icon(
            painter = painterResource(id = R.drawable.listen_volume_up_24),
            modifier = Modifier.scale(2.0f),
            contentDescription = "Start listening"
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LearningViewPreview() {
    LearningView(
        navController = NavController(LocalContext.current),
        numbers = (0..100).toList(),
        locale = Locale.US
    )
}