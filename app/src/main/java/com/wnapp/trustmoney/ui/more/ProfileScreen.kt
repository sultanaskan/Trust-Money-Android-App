package com.wnapp.trustmoney.ui.more

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar

/**
 * সোর্স: Read-only Data Display
 * কনসেপ্ট: Information Hierarchy
 * কাজ: ইউজারের পার্সোনাল ডেটা প্রদর্শন করা।
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile Information") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            ProfileItem(label = "Full Name", value = "MD RASEL MOLLAH")
            ProfileItem(label = "Email", value = "rasel@example.com")
            ProfileItem(label = "Phone", value = "+880123456789")

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Update Profile")
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
    }
}