package com.github.pokatomnik.kriper.screens.addbookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.ConfirmationDialog
import com.github.pokatomnik.kriper.ui.components.ConfirmationDialogButton
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.makeToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddBookmark(
    storyId: String,
    scrollPosition: Int,
    onNavigateBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val addBookmark = rememberAddBookmark()
    val showToast = makeToast()

    val (exitConfirmationDialogVisible, setExitConfirmationDialogVisible) = remember {
        mutableStateOf(false)
    }

    val focusRequester = remember { FocusRequester() }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val requestFocus = {
        focusRequester.requestFocus()
        softwareKeyboardController?.show()
    }

    IndexServiceReadiness { indexService ->
        indexService.content.getPageMetaByStoryId(storyId)?.let { pageMeta ->
            val (bookmarkName, setBookmarkName) = remember {
                mutableStateOf("")
            }

            val handleBackPress: () -> Unit = {
                if (bookmarkName.isBlank()) {
                    setExitConfirmationDialogVisible(false)
                    onNavigateBack()
                } else {
                    setExitConfirmationDialogVisible(true)
                }
            }

            val handleSavePress: () -> Unit = {
                if (bookmarkName.isBlank()) {
                    showToast("Введите имя закладки")
                } else {
                    setExitConfirmationDialogVisible(false)
                    coroutineScope.launch {
                        addBookmark(bookmarkName, storyId, scrollPosition)
                        showToast("Закладка создана")
                        onNavigateBack()
                    }
                }
            }

            LaunchedEffect(Unit) {
                requestFocus()
            }

            PageContainer(
                priorButton = {
                    IconButton(onClick = handleBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                header = { PageTitle(title = "Создать закладку") },
                trailingButton = {
                    IconButton(onClick = handleSavePress) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Сохранить"
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LARGE_PADDING.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(LARGE_PADDING.dp))
                    Text(
                        text = "Создаем закладку для истории \"${pageMeta.title}\"",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(LARGE_PADDING.dp))
                    Text(text = "Название закладки")
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = bookmarkName,
                        onValueChange = { next ->
                            setBookmarkName(next)
                        },
                        placeholder = {
                            Text(text = getPlaceholder(pageMeta.title))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { handleSavePress() }
                        )
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(LARGE_PADDING.dp))
                }
            }
            if (exitConfirmationDialogVisible) {
                ConfirmationDialog(
                    title = "Сохранить закладку?",
                    description = "Вы не сохранили закладку, сохранить?",
                    confirm = object : ConfirmationDialogButton {
                        override val title = "Да"
                        override fun onPress() {
                            handleSavePress()
                        }
                    },
                    cancel = object : ConfirmationDialogButton {
                        override val title = "Нет"
                        override fun onPress() {
                            onNavigateBack()
                        }
                    },
                    onClickOutside = {
                        setExitConfirmationDialogVisible(false)
                    }
                )
            }
        }
    }
}

fun getPlaceholder(storyTitle: String): String {
    return "Закладка для \"$storyTitle\""
}