package com.wnapp.trustmoney.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wnapp.trustmoney.ui.theme.PrimaryBlue
import com.wnapp.trustmoney.R
/**
 * সোর্স: Jetpack Compose OutlinedTextField
 * কনসেপ্ট: Reusable Component (পুনঃব্যবহারযোগ্য উপাদান)
 * কাজ: অ্যাপের সব জায়গায় একই স্টাইলের ইনপুট বক্স নিশ্চিত করা।
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false // পাসওয়ার্ড হলে টেক্সট হাইড করার অপশন
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp), // কোণাগুলো একটু গোল করার জন্য
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = PrimaryBlue, // ক্লিক করলে বর্ডারের কালার নীল হবে
            focusedLabelColor = PrimaryBlue
        )
    )
}