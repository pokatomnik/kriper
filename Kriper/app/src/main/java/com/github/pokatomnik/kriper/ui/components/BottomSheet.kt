package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    height: Int? = null,
    drawerState: BottomDrawerState,
    content: @Composable () -> Unit,
    drawerContent: @Composable () -> Unit,
) {
    BottomDrawer(
        scrimColor = Color.Transparent,
        gesturesEnabled = drawerState.isOpen,
        drawerState = drawerState,
        drawerBackgroundColor = Color.Transparent,
        drawerContent = {
            BottomDrawerSurface(height = height, content = drawerContent)
        },
        content = content
    )
}

@Composable
private fun BottomDrawerSurface(
    height: Int?,
    content: @Composable () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.background,
        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
    ) {
        var wrapperModifier = Modifier.fillMaxWidth()
        wrapperModifier =
            (if (height != null) wrapperModifier.height(height.dp) else wrapperModifier.fillMaxHeight(0.5f))
        Column(modifier = wrapperModifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(3.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                content()
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            }
        }
    }
}