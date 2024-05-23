package com.xiaoyv.comic.flexiflix.ui.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings.GLOBAL_SP_NAME
import com.xiaoyv.flexiflix.extension.utils.get
import com.xiaoyv.flexiflix.extension.utils.put


/**
 * 输入类型的设置项
 */
@Composable
fun SettingInputItem(
    key: String,
    default: String? = null,
    title: String = key,
    subtitle: String = "",
    icon: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val preference = remember { context.getSharedPreferences(GLOBAL_SP_NAME, Context.MODE_PRIVATE) }
    var initValue by remember(key) { mutableStateOf(preference.getString(key, default).orEmpty()) }

    var dialogState by remember(key) { mutableStateOf(false) }
    if (dialogState) {
        BasicAlertDialog(onDismissRequest = { dialogState = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = AlertDialogDefaults.containerColor,
                        shape = AlertDialogDefaults.shape
                    )
                    .padding(vertical = 12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    value = initValue,
                    onValueChange = {
                        initValue = it
                        preference.put(key, it)
                    }
                )
            }
        }
    }


    SettingNormalItem(
        title = title,
        subtitle = subtitle,
        icon = icon,
        onItemClick = {
            dialogState = true
        }
    )
}

/**
 * 选项类型的设置项
 */
@Composable
fun <T> SettingOptionsItem(
    key: String,
    formatClass: Class<T>,
    values: Map<String, T> = emptyMap(),
    default: T? = null,
    title: String = key,
    subtitle: (String, T?) -> String = { label, _ -> label },
    icon: (@Composable () -> Unit)? = null,
    onValueChange: (T) -> Unit = {},
) {
    val context = LocalContext.current
    val preference = remember { context.getSharedPreferences(GLOBAL_SP_NAME, Context.MODE_PRIVATE) }
    val rememberEntries = remember(key) { values.entries.toList() }
    var initValue by remember(key) { mutableStateOf(preference.get(key, formatClass, default)) }
    var initLabel by remember(key) {
        mutableStateOf(rememberEntries.find { it.value == initValue }?.key.orEmpty())
    }

    var dialogState by remember(key) { mutableStateOf(false) }
    if (dialogState) {
        BasicAlertDialog(onDismissRequest = { dialogState = false }) {
            LazyList(
                modifier = Modifier
                    .background(
                        color = AlertDialogDefaults.containerColor,
                        shape = AlertDialogDefaults.shape
                    )
                    .padding(vertical = 12.dp)
            ) {
                items(rememberEntries) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                initValue = it.value
                                initLabel = it.key

                                preference.put(key, it.value)

                                onValueChange(it.value)

                                dialogState = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        text = it.key
                    )
                }
            }
        }
    }


    SettingNormalItem(
        title = title,
        subtitle = subtitle(initLabel, initValue),
        icon = icon,
        onItemClick = {
            dialogState = true
        }
    )
}

/**
 * 普通文案类型设置
 *
 * @author why
 * @since 5/22/24
 */
@Composable
fun SettingNormalItem(
    title: String,
    subtitle: String = "",
    icon: (@Composable () -> Unit)? = null,
    onItemClick: () -> Unit = {},
) {

    Row(
        modifier = Modifier
            .clickable { onItemClick() }
            .fillMaxWidth()
            .height(if (subtitle.isBlank()) 60.dp else 70.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (icon != null) Box(Modifier.padding(horizontal = 6.dp)) {
            icon()
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = if (icon == null) 0.dp else 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (subtitle.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 开关类型的设置项
 */
@Composable
fun SettingSwitchItem(
    key: String,
    default: Boolean = false,
    title: String = key,
    subtitle: String = "",
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    onValueChange: (Boolean) -> Unit = {},
) {

    val context = LocalContext.current
    val preference = remember { context.getSharedPreferences(GLOBAL_SP_NAME, Context.MODE_PRIVATE) }
    var initValue by remember(key) {
        mutableStateOf(
            preference.get(
                key = key,
                cls = Boolean::class.javaPrimitiveType,
                default = default
            )
        )
    }

    onValueChange(initValue)

    Row(
        modifier = Modifier
            .clickable {
                if (enabled) {
                    initValue = !initValue

                    preference.put(key, initValue)
                    onValueChange(initValue)
                }
            }
            .fillMaxWidth()
            .height(if (subtitle.isBlank()) 60.dp else 70.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) Box(Modifier.padding(horizontal = 6.dp)) {
            icon()
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = if (icon == null) 0.dp else 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (subtitle.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Switch(
            checked = initValue,
            onCheckedChange = {
                if (enabled) {
                    initValue = it

                    preference.put(key, it)
                    onValueChange(it)
                }
            },
            enabled = enabled,
        )
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 700)
fun PreviewSettings() {
    var switch by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        SettingNormalItem(
            title = "帮助",
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        SettingSwitchItem(
            key = "test",
            title = "帮助",
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            onValueChange = {
                switch = it
            }
        )

        SettingNormalItem(
            title = "帮助",
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        SettingNormalItem(
            title = "帮助",
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}