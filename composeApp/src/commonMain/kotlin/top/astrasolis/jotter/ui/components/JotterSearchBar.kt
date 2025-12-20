package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.SearchBar

/**
 * Jotter 搜索栏组件
 * 封装 Miuix SearchBar，提供统一的搜索体验
 */
@Composable
fun JotterSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    searchResults: @Composable (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    
    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.screenH),
        inputField = {
            InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch()
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                enabled = enabled,
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        // 搜索结果内容
        if (searchResults != null) {
            Column {
                searchResults()
            }
        }
    }
}
