package com.ronjunevaldoz.todo.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    inputType: KeyboardType = KeyboardType.Text
) {
    val isTypePassword = inputType == KeyboardType.Password

    val visualTransformation = if (isTypePassword) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    OutlinedTextField(
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = inputType),
        value = value,
        enabled = enabled,
        leadingIcon = leadingIcon,
        textStyle = TextStyle(fontSize = 18.sp),
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(text = placeholder, style = TextStyle(fontSize = 18.sp, color = Color.LightGray))
        }
    )
}