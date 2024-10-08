package com.maulana.warehouse.core.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maulana.warehouse.util.GlobalDimension

@Composable
fun DetailItem(
    key: String,
    value: String,
    isEditMode: Boolean = false,
    isNumeric: Boolean = false,
    onEditValue: (String) -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            text = key,
            fontSize = GlobalDimension.defaultFontSize, modifier = Modifier.fillMaxWidth(0.3f)
        )
        Spacer(4.dp)
        Text(
            text = ":",
            fontSize = GlobalDimension.defaultFontSize
        )
        Spacer(8.dp)
        if (isEditMode) {
            TextField(value = value,
                keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
                onValueChange = {newValue->
                    if (isNumeric) {
                        onEditValue(newValue.filter { it.isDigit() })
                    } else {
                        onEditValue(newValue)
                    }
                })
        }
        Text(
            text = value,
            fontSize = GlobalDimension.defaultFontSize
        )
    }
}