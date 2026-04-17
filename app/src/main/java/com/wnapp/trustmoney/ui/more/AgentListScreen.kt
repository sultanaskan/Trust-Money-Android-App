package com.wnapp.trustmoney.ui.more

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * সোর্স: LazyColumn with Custom List Items
 * কনসেপ্ট: Location-based Information Display
 * কাজ: ইউজারের আশেপাশে থাকা এজেন্টদের ঠিকানা ও নাম দেখানো।
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentListScreen() {
    // ডামি ডেটা (Source: Static Data Placeholder)
    val agents = listOf("London Express", "Birmingham Money Center", "Manchester Agent", "Oldham Services")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Our Agent List") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(agents) { agent ->
                ListItem(
                    headlineContent = { Text(agent) },
                    supportingContent = { Text("Open: 9:00 AM - 6:00 PM") },
                    leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingContent = { TextButton(onClick = {}) { Text("View Map") } }
                )
                HorizontalDivider()
            }
        }
    }
}