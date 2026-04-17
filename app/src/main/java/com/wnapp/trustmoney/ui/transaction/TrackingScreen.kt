package com.wnapp.trustmoney.ui.transaction


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wnapp.trustmoney.ui.theme.PrimaryBlue

/**
 * সোর্স: Custom Step Indicator / Timeline UI
 * কনসেপ্ট: Visual Progress Tracking
 * কাজ: ট্রানজ্যাকশনের বর্তমান অবস্থা (যেমন: Pending, Dispatched, Received) ইউজারকে দেখানো।
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Transaction") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(20.dp)) {
            // ট্র্যাকিং আইডি ইনপুট বক্স
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Enter Transaction ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // টাইমলাইন ডিজাইন (একটি উদাহরণ)
            TrackStep("Order Placed", "10 April 2026, 10:30 AM", isCompleted = true)
            TrackStep("Processing", "11 April 2026, 02:15 PM", isCompleted = true)
            TrackStep("Out for Delivery", "Waiting...", isCompleted = false)
        }
    }
}

@Composable
fun TrackStep(title: String, subtitle: String, isCompleted: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isCompleted) PrimaryBlue else Color.Gray // সম্পন্ন হলে নীল, না হলে ধূসর
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}