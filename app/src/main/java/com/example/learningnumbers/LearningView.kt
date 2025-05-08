@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.learningnumbers

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.learningnumbers.ui.theme.Button_color_disabled
import com.example.learningnumbers.ui.theme.Button_color_enabled
import com.example.learningnumbers.ui.theme.List_color_dark
import com.example.learningnumbers.ui.theme.Purple40
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningView(navController: NavController, numbers: List<Int>, locale: Locale) {
    var topBarHeight by remember { mutableIntStateOf(0) }
    val textColor = Color.Black
    val textSize = 24.sp
    var currentNumberIndex by remember { mutableIntStateOf(0) }
    val currentNumber = numbers.elementAtOrNull(currentNumberIndex)
    val sortedNumbers = numbers.sorted()
    val textRange = IntRange(sortedNumbers.first(), sortedNumbers.last()).toString()
    var speedRate by remember { mutableFloatStateOf(1.0f) }
    var delayTime by remember { mutableLongStateOf(500) }
    var enabledSpeak by remember { mutableStateOf(true) }
    val ttsViewModel = TtsViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.onGloballyPositioned { topBarHeight = it.size.height },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Learning View",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40),
                navigationIcon = {
                    IconButton(onClick = {
                        ttsViewModel.stopSpeaking()
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
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

            if (enabledSpeak && currentNumber != null && currentNumberIndex < numbers.count()) {
                Spacer(modifier = Modifier.padding(24.dp))
                ListenButton(
                    context = ctx,
                    currentNumber,
                    locale,
                    ttsViewModel = ttsViewModel,
                    onListenDone = { success ->
                        if (success) {
                            currentNumberIndex++
                        }
                    })
            } else {
                Text(
                    modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
                    text = if (enabledSpeak) "No more numbers" else "Playback speaking",
                    color = textColor, fontSize = textSize
                )
            }
            Spacer(modifier = Modifier.padding(24.dp))
            PlaybackButton(
                numbers, locale, speedRate, delayTime, ttsViewModel = ttsViewModel,
                onPlaying = { it -> enabledSpeak = !it })
            if (enabledSpeak) {
                Spacer(modifier = Modifier.padding(24.dp))
                Row(
                    modifier = Modifier
                        .wrapContentWidth(unbounded = true)
                        .focusable(enabledSpeak)
                ) {
                    // Speed rate selector
                    ListDownSelector(
                        defaultValue = 1.0f,
                        listItems = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f),
                        onCallbackValue = {
                            speedRate = it
                        })
                    ListDownSelector(
                        defaultValue = 500L, // delay in milliseconds
                        listItems = listOf(
                            500L,
                            1000L,
                            1500L,
                            2000L,
                            3000L,
                            5000L
                        ),  // speech delays list
                        onCallbackValue = {
                            delayTime = it
                        })
                }
            }
        }
    }
}

@Composable
fun ListenButton(
    context: Context,
    number: Int,
    locale: Locale,
    ttsViewModel: TtsViewModel,
    onListenDone: ((Boolean) -> Unit)?
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .size(90.dp),
        onClick = {
            ttsViewModel.listenSinglePhrase(
                number.toString(), locale, context,
                onFinishedSpeech = { success -> onListenDone?.invoke(success) })
        }) {
        Icon(
            painter = painterResource(id = R.drawable.playback_numbers_24),
            modifier = Modifier
                .scale(4.0f)
                .background(color = Button_color_enabled),
            contentDescription = "Start listening"
        )
    }
}

@Composable
fun PlaybackButton(
    numbers: List<Int>,
    locale: Locale,
    speechRate: Float = 1.0f,
    delayMillis: Long = 500,
    ttsViewModel: TtsViewModel,
    onPlaying: ((Boolean) -> Unit)?,
) {
    val context = LocalContext.current
    var currentSpokenNumber by remember { mutableStateOf<Int?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .size(90.dp),
        onClick = {
            isPlaying = true
            ttsViewModel.playNumbersSequence(
                numbers, locale, speechRate, context, delayMillis,
                onSequenceFinished = { success ->
                    isPlaying = success
                    currentSpokenNumber = null
                },
                onNumberSpoken = { number, _ ->
                    // Update UI with the number being spoken
                    currentSpokenNumber = number
                })
        },
        enabled = !isPlaying
    ) {
        Icon(
            painter = painterResource(id = R.drawable.playback_numbers_24),
            modifier = Modifier
                .scale(4.0f)
                .background(color = if (isPlaying) Button_color_disabled else Button_color_enabled),
            contentDescription = "Start listening"
        )
        if (isPlaying) {
            onPlaying?.invoke(true)
        }
    }
    if (currentSpokenNumber != null) {
        Text(text = "Speaking: $currentSpokenNumber")
    } else {
        onPlaying?.invoke(false)
        isPlaying = false
    }
}

@Composable
fun <T> ListDownSelector(
    defaultValue: T,
    listItems: List<T>,
    onCallbackValue: (T) -> Unit
) {
    val textSize = 24.sp
    val cornerShape = RoundedCornerShape(24.dp)
    val itemHeight = 40.dp//48.dp
    val textHorizontalPadding = 12.dp
    val backGroundColder = List_color_dark

    Column(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.75f)
            .padding(horizontal = 64.dp)
            .wrapContentHeight(unbounded = true)
    )
    {
        var isExtended by remember { mutableStateOf(false) }
        var selectedValue: T by remember { mutableStateOf(defaultValue) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = backGroundColder,
                    shape = cornerShape
                )
                .clickable {
                    isExtended = !isExtended
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = textHorizontalPadding),
                text = selectedValue.toString(),
                fontSize = textSize
            )
            Icon(
                modifier = Modifier.padding(end = textHorizontalPadding),
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = null
            )
        }

        if (isExtended) {
            for (value in listItems) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = backGroundColder,
                            shape = cornerShape
                        )
                        .height(itemHeight)
                        .clickable {
                            selectedValue = value
                            onCallbackValue(value)
                            isExtended = !isExtended
                        }) {
                    Text(
                        text = value.toString(),
                        modifier = Modifier
                            .padding(horizontal = textHorizontalPadding)
                            .align(alignment = Alignment.Center),
                        fontSize = textSize,
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LearningViewPreview() {
    LearningView(
        navController = NavController(LocalContext.current),
        numbers = (0..100).toList(),
        locale = Locale.US
    )
}