package com.xiaoyv.comic.flexiflix.ui.component

import androidx.annotation.IntRange
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
@NonRestartableComposable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
) = androidx.compose.material3.Button(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(containerColor = color)
) {
    Text(
        text = text,
        overflow = TextOverflow.Clip,
        color = textColor,
        softWrap = false,
    )
}

/**
 * 一列和两列三列等切换开关
 */
@Composable
fun ColumnsToggleButton(@IntRange(from = 1, to = 2) columns: Int, onChange: (Int) -> Unit = {}) {
    IconButton(onClick = { if (columns == 1) onChange(2) else onChange(1) }) {
        if (columns == 1) {
            Icon(imageVector = Icons.Default.ViewModule, null)
        } else {
            Icon(imageVector = Icons.AutoMirrored.Default.ViewList, null)
        }
    }
}