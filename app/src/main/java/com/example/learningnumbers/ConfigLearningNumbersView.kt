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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.learningnumbers.ui.theme.Purple40
import com.example.learningnumbers.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigLearningNumbersView(
    navController: NavController,
    name: String,
    viewModel: ViewModelNumbers
) {
    val languages = viewModel.availableLanguages()

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(name) },
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
                onSelectedLanguage = { it: Language -> viewModel.selectLanguage(it) })
            Spacer(modifier = Modifier.padding(spacer))
            NumbersRangeSelector(
                numbersRange = viewModel.numbersRanges,
                selectedNumbersRange = viewModel.selectedNumbersRange,
                onNumbersRange = { range: IntProgression -> viewModel.updatedNumbersRange(range) })
            Spacer(modifier = Modifier.padding(spacer))
            CheckboxWithLabel(onRandomize = { random: Boolean ->
                viewModel.updateRandomizeNumbers(random)
            })
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

@Composable
fun NumbersRangeSelector(
    numbersRange: List<IntProgression>,
    selectedNumbersRange: LiveData<IntProgression>,
    onNumbersRange: (IntProgression) -> Unit
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
            mutableStateOf(selectedNumbersRange.value)
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
                text = formatRangeWithoutStep(uiSelectedNumbersRange),
                fontSize = textSize
            )
            Icon(
                modifier = Modifier.padding(end = textHorizontalPadding),
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = null
            )
        }

        if (isExtended) {
            for (progress in numbersRange) {
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
                            uiSelectedNumbersRange = progress
                            onNumbersRange(progress)
                            isExtended = !isExtended
                        }) {
                    Text(
                        text = formatRangeWithoutStep(progress),
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

fun formatRangeWithoutStep(progression: IntProgression?): String {
    // Note: This doesn't reflect whether it was originally .. or until or downTo.
    // It just shows the first and last elements.
    return if (progression != null) {
        "${progression.first} .. ${progression.last}"
    } else {
        return "wrong range"
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConfigLearningNumbersViewPreview() {
    LearningNumbersTheme {
        val navController = rememberNavController()
        ConfigLearningNumbersView(
            navController,
            name = LocalContext.current.resources.getString(R.string.titleConfigLearningNumbersView),
            viewModel = ViewModelNumbers()
        )
    }
}