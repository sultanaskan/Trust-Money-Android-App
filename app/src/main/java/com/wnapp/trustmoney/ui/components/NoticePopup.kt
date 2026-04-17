package com.wnapp.trustmoney.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * সোর্স: Material 3 AlertDialog
 * কনসেপ্ট: Modal UI Component
 * কাজ: ইউজারকে জরুরি নোটিশ বা আপডেট দেখানো।
 */
@Composable
fun NoticePopup(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}