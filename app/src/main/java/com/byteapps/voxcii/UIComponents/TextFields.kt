package com.byteapps.voxcii.UIComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.byteapps.voxcii.ui.theme.AppTheme

@Composable
fun CustomTextField(
    modifier: Modifier,
    value:(Any)->Unit,
    label:String,
    placeHolder:String,
    keyboardOptions: KeyboardOptions =  KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done // Ensure "Done" action is set
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = {
            // Hide the keyboard after submission
        }
    )
) {

    var fieldValue by remember {
        mutableStateOf("")
    }

    Column(

        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Text(
            text = label,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colorScheme.onTertiary
        )
        OutlinedTextField(
            modifier = modifier,
            value = fieldValue,
            onValueChange = {
                value(it)
                fieldValue = it
            },
            placeholder = {
                Text(
                    text = placeHolder,
                    color = AppTheme.colorScheme.onSecondary
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AppTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.LightGray,
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(
                color = AppTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }




}
