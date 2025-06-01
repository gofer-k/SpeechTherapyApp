package com.example.learningnumbers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
