package com.maulana.warehouse.core.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ChecklistItem(checked: Boolean, title: String, onCheckedChange: (Boolean) -> Unit) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(
            text = title,
            textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
        )
    }
}