package com.maulana.warehouse.core.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maulana.warehouse.util.GlobalDimension


@Composable
fun OutlinedPasswordField(
    password: String,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit
) {
    val passwordVisibility = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        onValueChange = {
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Password")
        },
        trailingIcon = {
            Icon(
                imageVector = if (passwordVisibility.value) Icons.Default.Lock else Icons.Default.Face,
                contentDescription = "back",
                modifier = Modifier
                    .padding(end = 22.dp, start = 8.dp)
                    .clickable {
                        passwordVisibility.value = !passwordVisibility.value
                    }
            )
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }),
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Composable
fun PasswordField(
    password: String,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit
) {
    val passwordVisibility = remember { mutableStateOf(false) }
    TextField(
        value = password,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        onValueChange = {
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text("******", fontSize = GlobalDimension.defaultFontSize) },
        leadingIcon = { Icon(Icons.Default.Lock, "password icon") },
        trailingIcon = {
            Icon(
                imageVector = if (passwordVisibility.value) Icons.Default.Lock else Icons.Default.Face,
                contentDescription = "back",
                modifier = Modifier
                    .padding(end = 22.dp, start = 8.dp)
                    .clickable {
                        passwordVisibility.value = !passwordVisibility.value
                    }
            )
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(true) }),
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Preview(showBackground = true)
@Composable
private fun InternalPreview() {
    MaterialTheme() {
        OutlinedPasswordField(
            password = "assdd",
            focusManager = LocalFocusManager.current,
            onValueChange = {}
        )
    }
}