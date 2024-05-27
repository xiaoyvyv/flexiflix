package com.xiaoyv.comic.flexiflix.ui.screen.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import com.xiaoyv.comic.flexiflix.model.SearchRequestParam
import com.xiaoyv.comic.flexiflix.ui.component.AppBar
import com.xiaoyv.comic.flexiflix.ui.component.Button
import com.xiaoyv.comic.flexiflix.ui.component.LazyList
import com.xiaoyv.comic.flexiflix.ui.component.ScaffoldScreen
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.model.hasData
import com.xiaoyv.flexiflix.common.model.payload
import com.xiaoyv.flexiflix.extension.model.FlexKeyValue
import com.xiaoyv.flexiflix.extension.model.FlexSearchOption
import com.xiaoyv.flexiflix.extension.model.FlexSearchOptionItem

/**
 * [MediaSearchScreen]
 *
 * @author why
 * @since 5/18/24
 */
@Composable
fun MediaSearchRoute(
    onNavUp: () -> Unit = {},
    onSearch: (String, SearchRequestParam) -> Unit = { _, _ -> },
) {
    val videModel = hiltViewModel<MediaSearchViewModel>()
    val searchOptions by videModel.searchOptionsState.collectAsStateWithLifecycle()
    val selectedOptions by videModel.selectedOptions.collectAsStateWithLifecycle()
    val query by videModel.keyword.collectAsStateWithLifecycle()

    MediaSearchScreen(
        title = videModel.args.sourceName,
        query = query,
        onQueryChange = { videModel.onKeywordChange(it) },
        searchOptions = searchOptions,
        selectedOptions = selectedOptions,
        onNavUp = onNavUp,
        onSearch = {
            videModel.onKeywordChange(it)
            onSearch(videModel.args.sourceId, videModel.buildParam())
        },
        onSelectedOptionsChange = { option, selected ->
            videModel.onSelectedValuesChange(option, selected)
        },
    )
}

@Composable
fun MediaSearchScreen(
    title: String,
    searchOptions: MediaSearchState,
    query: String,
    onQueryChange: (String) -> Unit = {},
    selectedOptions: List<FlexSearchOptionItem>,
    onSelectedOptionsChange: (FlexSearchOptionItem, List<FlexKeyValue>) -> Unit = { _, _ -> },
    onSearch: (String) -> Unit = {},
    onNavUp: () -> Unit = {},
) {
    ScaffoldScreen(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp),
                            value = query,
                            onValueChange = { onQueryChange(it) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                            singleLine = true,
                            decorationBox = @Composable { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceContainer,
                                            shape = MaterialTheme.shapes.extraLarge,
                                        )
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                ) {
                                    if (query.isEmpty()) {
                                        Text(
                                            text = "搜索（$title）",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search,
                                autoCorrect = false,
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    onSearch(query)
                                }
                            ),
                        )

                        Button(
                            text = "搜索",
                            onClick = {
                                onSearch(query)
                            }
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        // 选项
        if (searchOptions.data.hasData) {
            MediaSearchMenus(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it),
                options = searchOptions.data.payload().options.orEmpty(),
                selectedOptions = selectedOptions,
                onSelectedValuesChange = onSelectedOptionsChange
            )
        }
    }
}

@Composable
fun MediaSearchMenus(
    modifier: Modifier,
    options: List<FlexSearchOptionItem>,
    selectedOptions: List<FlexSearchOptionItem>,
    onSelectedValuesChange: (FlexSearchOptionItem, List<FlexKeyValue>) -> Unit = { _, _ -> },
) {
    LazyList(modifier = modifier) {
        items(options) { option ->
            // 选中的数据
            val selected = remember(option.key) {
                val cached = selectedOptions.find { it.key == option.key }?.values?.toTypedArray()
                mutableStateListOf(*(cached ?: emptyArray()))
            }
            val optionValues = remember(option.key) { option.values.orEmpty() }

            val maxShow = 20
            var expland by remember(option.key) { mutableStateOf(false) }

            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = option.keyLabel ?: option.key,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                if (optionValues.size > maxShow) TextButton(onClick = { expland = !expland }) {
                    Icon(
                        imageVector = if (expland) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                    Text(
                        text = if (expland) "收起" else "展开",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            FlowRow(
                modifier = Modifier.padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                optionValues.forEachIndexed { index, value ->
                    if (expland.not() && index >= maxShow) {
                        return@forEachIndexed
                    }

                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (selected.contains(value)) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
                            contentColor = if (selected.contains(value)) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified
                        ),
                        onClick = click@{
                            if (selected.contains(value)) {
                                selected.remove(value)
                                onSelectedValuesChange(option, selected.toList())
                                return@click
                            }

                            // 不限制
                            if (option.maxSelect <= 0 || option.maxSelect > selected.size) {
                                selected.add(value)
                                onSelectedValuesChange(option, selected.toList())
                            } else {
                                // 选取达到 maxSelect
                                // 针对 maxSelect = 1 的情况，单独优化为 toggle 效果
                                if (option.maxSelect == 1) {
                                    selected.clear()
                                    selected.add(value)
                                    onSelectedValuesChange(option, selected.toList())
                                }
                            }
                        },
                    ) {
                        Text(text = value.value)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 411)
fun PreviewMediaSearchScreen() {
    MediaSearchScreen(
        title = "测试数据源",
        query = "测试",
        searchOptions = MediaSearchState(
            loadState = LoadState.NotLoading(true),
            data = StateContent.Payload(
                FlexSearchOption(
                    keywordKey = "xxx",
                    options = listOf(
                        FlexSearchOptionItem(
                            key = "",
                            keyLabel = "排序",
                            values = listOf(
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                            )
                        ),
                        FlexSearchOptionItem(
                            key = "",
                            keyLabel = "排序",
                            values = listOf(
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                                FlexKeyValue("", "选项1"),
                            )
                        ),
                        FlexSearchOptionItem(
                            key = "",
                            keyLabel = "排序"
                        ),
                        FlexSearchOptionItem(
                            key = "",
                            keyLabel = "排序"
                        ),
                    )
                )
            )
        ),
        selectedOptions = emptyList(),

        )
}