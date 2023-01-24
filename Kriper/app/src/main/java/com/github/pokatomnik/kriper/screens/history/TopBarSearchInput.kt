package com.github.pokatomnik.kriper.screens.history

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.TextFieldDecorationBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarSearchInput(
    searchTextState: MutableState<String>,
    onSearchButtonPress: () -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {
    val searchTextColor = contentColorFor(MaterialTheme.colors.primarySurface)
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = searchTextState.value,
        onValueChange = { searchTextState.value = it },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchButtonPress() }
        ),
        maxLines = 1,
        cursorBrush = SolidColor(searchTextColor),
        textStyle = TextStyle(color = searchTextColor),
        decorationBox = {
            TextFieldDecorationBox(
                value = searchTextState.value,
                innerTextField = it,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    Text(
                        text = "Поиск",
                        color = searchTextColor
                    )
                }
            )
        }
    )
}