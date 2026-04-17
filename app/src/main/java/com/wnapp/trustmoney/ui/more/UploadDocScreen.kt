package com.wnapp.trustmoney.ui.more

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * সোর্স: Android Storage Access Framework (SAF)
 * কনসেপ্ট: File Picker Integration
 * কাজ: ইউজারের ফোন থেকে ইমেজ বা পিডিএফ ফাইল সিলেক্ট করা।
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDocScreen(onBack: () -> Unit) {
    // ইউজারের সিলেক্ট করা ফাইলের লোকেশন (Uri) মনে রাখার জন্য
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    // ফাইল পিকার লঞ্চার (এটি গ্যালারি বা ফাইল ম্যানেজার ওপেন করে)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri // ফাইল সিলেক্ট করলে সেটির পাথ সেভ হবে
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Upload Documents") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Please upload your NID or Passport copy.")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { launcher.launch("image/*") }, // শুধু ছবি সিলেক্ট করার জন্য
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (selectedFileUri == null) "Select Image" else "Image Selected ✅")
            }

            if (selectedFileUri != null) {
                Text("Selected: ${selectedFileUri?.path?.takeLast(30)}...", modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* সার্ভারে পাঠানোর কোড */ },
                enabled = selectedFileUri != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Now")
            }
        }
    }
}