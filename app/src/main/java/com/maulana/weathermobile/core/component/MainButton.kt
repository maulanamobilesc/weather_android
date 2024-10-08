package com.maulana.warehouse.core.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maulana.warehouse.util.GlobalDimension

@Composable
fun MainButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text,
            fontSize = GlobalDimension.mainButtonFontSize,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}