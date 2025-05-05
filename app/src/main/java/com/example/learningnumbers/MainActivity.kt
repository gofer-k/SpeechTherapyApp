package com.example.learningnumbers

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningnumbers.ui.theme.BackGroundGradientEnd
import com.example.learningnumbers.ui.theme.BackGroundGradientStart
import com.example.learningnumbers.ui.theme.LearningNumbersTheme
import com.example.learningnumbers.ui.theme.List_color
import com.example.learningnumbers.ui.theme.List_color_dark
import java.util.Locale
import androidx.compose.runtime.remember as remember1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningNumbersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainViewContent(
                        name = "Multilingual Numbers",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainViewContent(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(BackGroundGradientStart, BackGroundGradientEnd))
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var selectedNUmbersRange by remember1 { mutableStateOf(0..100) }
        var selectedLanguange by remember1 { mutableStateOf(Language("English", Locale("en"))) }

        Icon(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .semantics { this.contentDescription = "fveer" }
                .size(124.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = modifier)
        Spacer(modifier = Modifier.padding(10.dp))
        LanguageSelector(
            // TODO: save languages into viewModel
            languages = listOf(
                Language("English", Locale("en")),
                Language("polish", Locale("pl"))
            ),
            // TODO: save language into viewModel
            viewModelLanguage = Language("English", Locale("en")),
            onSelectedLanguage = { it -> selectedLanguange = it })
        Spacer(modifier = Modifier.padding(10.dp))
        NumbersRangeSelector(onNumbersRange = { it -> selectedNUmbersRange = it })
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = {},
            shape = RoundedCornerShape(size = 12.dp)
        ) {
            Text(text = "Start learning")
        }
    }
}

@Composable
fun NumbersRangeSelector(onNumbersRange: (IntRange) -> Unit) {
    val textSize = 20.sp
    val cornerShape = RoundedCornerShape(24.dp)
    val itemHeight = 40.dp//48.dp
    val textHorizontalPadding = 12.dp
    val backGroundColder = if (isSystemInDarkTheme()) List_color_dark else List_color

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .wrapContentHeight(unbounded = true)
    )
    {
        var isExtended by remember1 { mutableStateOf(false) }
        var selectedRange by remember1 { mutableStateOf(0..100) }
        var listRanges = listOf(
            0..10,
            0..100,
            0..1000,
            10..100,
            10..1000,
            100..1000
        )
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
                text = selectedRange.toString(),
                fontSize = textSize
            )
            Icon(
                modifier = Modifier.padding(end = textHorizontalPadding),
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = null
            )
        }

        if (isExtended) {
            for (range in listRanges) {
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
                            selectedRange = range
                            onNumbersRange(range)
                            isExtended = !isExtended
                        }) {
                    Text(
                        text = range.toString(),
                        modifier = Modifier
                            .padding(horizontal = textHorizontalPadding)
                            .align(alignment = Alignment.Center),
                        fontSize = textSize,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
                }
            }
        }
    }
}

// TODO: refactor selectors to generic component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    languages: List<Language>,
    viewModelLanguage: Language,
    onSelectedLanguage: (Language) -> Unit
) {
    val textSize = 20.sp
    val cornerShape = RoundedCornerShape(24.dp)
    val itemHeight = 40.dp//48.dp
    val textHorizontalPadding = 12.dp
    val backGroundColder = if (isSystemInDarkTheme()) List_color_dark else List_color

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .wrapContentHeight(unbounded = true)
    )
    {
        var isExtended by remember1 { mutableStateOf(false) }
        var selectedLanguage by remember1 { mutableStateOf(viewModelLanguage) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = List_color,
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
                text = selectedLanguage.label,
                fontSize = textSize
            )
            Icon(
                modifier = Modifier.padding(end = textHorizontalPadding),
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = null
            )
        }

        if (isExtended) {
            for (lang in languages) {
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
                            selectedLanguage = lang
                            onSelectedLanguage(lang)
                            isExtended = !isExtended
                        }) {
                    Text(
                        text = lang.label,
                        modifier = Modifier
                            .padding(horizontal = textHorizontalPadding)
                            .align(alignment = Alignment.Center),
                        fontSize = textSize,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainViewContentPreview() {
    LearningNumbersTheme {
        MainViewContent("Multilingual Numbers")
    }
}