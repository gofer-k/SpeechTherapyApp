package com.example.learningnumbers

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.learningnumbers.ui.theme.BackGroundGradientEnd
import com.example.learningnumbers.ui.theme.BackGroundGradientStart
import com.example.learningnumbers.ui.theme.LearningNumbersTheme
import com.example.learningnumbers.ui.theme.List_color_dark

@Composable
fun MainViewContent(
    navController: NavController,
    name: String,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
            val textColor = Color.Black
            val spacer = 10.dp
            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(124.dp)
            )
            Spacer(modifier = Modifier.padding(spacer))
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = modifier
            )
            Spacer(modifier = Modifier.padding(spacer))
            LanguageSelector(
                languages = viewModel.languages,
                language = viewModel.selectedLanguage,
                onSelectedLanguage = { viewModel.updatedLanguage(it) })
            Spacer(modifier = Modifier.padding(spacer))
            NumbersRangeSelector(
                viewModel.numbersRanges, viewModel.selectedNumbersRange,
                onNumbersRange = { viewModel.updatedNumbersRange(it) })
            Spacer(modifier = Modifier.padding(spacer))
            CheckboxWithLabel(onRandomize = {
                viewModel.updateRandimizeNumbers(it)
            })
            Spacer(modifier = Modifier.padding(spacer * 4))
            Button(
                onClick = {
                    navController.navigate("learning")
                },
                shape = RoundedCornerShape(size = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Start learning", fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun CheckboxWithLabel(onRandomize: ((Boolean) -> Unit)? = null) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable { isChecked = !isChecked } // Make the entire row clickable
            .padding(horizontal = 16.dp), // Add some padding
        verticalAlignment = Alignment.CenterVertically // Vertically align items in the row
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { newState ->
                isChecked = newState
                onRandomize?.invoke(isChecked)
            }
        )
        Text(
            text = "Random numbers",
            fontSize = 24.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .clickable {
                    isChecked = !isChecked
                    onRandomize?.invoke(isChecked)
                }
                .padding(start = 4.dp) // Add space between checkbox and text
        )
    }
}

@Composable
fun NumbersRangeSelector(
    numbersRange: List<IntRange>,
    selectedNumbersRange: LiveData<IntRange>,
    onNumbersRange: (IntRange) -> Unit
) {
    val textSize = 20.sp
    val cornerShape = RoundedCornerShape(24.dp)
    val itemHeight = 40.dp//48.dp
    val textHorizontalPadding = 12.dp
    val backGroundColder = List_color_dark

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .wrapContentHeight(unbounded = true)
    )
    {
        var isExtended by remember { mutableStateOf(false) }
        var uiSelectedNumbersRange by remember {
            mutableStateOf(
                selectedNumbersRange.value ?: (0..100)
            )
        }

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
                text = uiSelectedNumbersRange.toString(),
                fontSize = textSize
            )
            Icon(
                modifier = Modifier.padding(end = textHorizontalPadding),
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = null
            )
        }

        if (isExtended) {
            for (range in numbersRange) {
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
                            uiSelectedNumbersRange = range
                            onNumbersRange(range)
                            isExtended = !isExtended
                        }) {
                    Text(
                        text = range.toString(),
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

// TODO: refactor selectors to generic component
@Composable
fun LanguageSelector(
    languages: List<Language>,
    language: LiveData<Language>,
    onSelectedLanguage: (Language) -> Unit
) {
    val textSize = 20.sp
    val cornerShape = RoundedCornerShape(24.dp)
    val itemHeight = 40.dp//48.dp
    val textHorizontalPadding = 12.dp
    val backGroundColder = List_color_dark

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .wrapContentHeight(unbounded = true)
    )
    {
        var isExtended by remember { mutableStateOf(false) }
        var uiSelectedLanguage by remember { mutableStateOf(language.value ?: Language()) }

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
                text = uiSelectedLanguage.label,
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
                            uiSelectedLanguage = lang
                            onSelectedLanguage(lang)
                            isExtended = !isExtended
                        }) {
                    Text(
                        text = lang.label,
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

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainViewContentPreview() {
    LearningNumbersTheme {
        val navController = rememberNavController()
        MainViewContent(
            navController,
            viewModel = MainViewModel(),
            name = LocalContext.current.resources.getString(R.string.titleMainView)
        )
    }
}