package com.wnapp.trustmoney.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wnapp.trustmoney.ui.theme.BrandGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerifyScreen(onBack: () -> Unit) {
    var otpValue by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // ওটিপি ৪ ডিজিট হলে অটোমেটিক ডায়ালগ দেখাবে (টেস্ট করার জন্য)
    LaunchedEffect(otpValue) {
        if (otpValue.length == 4) {
            showSuccessDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "We just sent you a OTP",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "আমরা আপনার ইমেইল বা নাম্বার একটি কোড পাঠিয়েছি (+971000000000 এবং 00000000). আপনার অ্যাকাউন্ট সক্রিয় করতে এখানে কোডটি লিখুন।",
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ৪টি ওটিপি বক্স
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(4) { index ->
                    val char = otpValue.getOrNull(index)?.toString() ?: ""
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .background(Color(0xFFF9F9F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = char, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ইনপুট নেওয়ার জন্য হিডেন টেক্সট ফিল্ড
            TextField(
                value = otpValue,
                onValueChange = {
                    if (it.length <= 4) otpValue = it
                },
                modifier = Modifier.size(0.dp), // ইনভিজিবল
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text("Don't receive the activation code", color = Color.Black, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Resend logic */ },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) {
                Text("Resend code", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSuccessDialog) {
        RegistrationSuccessDialog(
            onContinue = { showSuccessDialog = false }
        )
    }
}




@Composable
fun RegistrationSuccessDialog(onContinue: () -> Unit) {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // সাকসেস চেক আইকন
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF2ECC71), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "REGISTRATION\nSUCCESSFUL",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your account has been securely created. For your safety, please keep your login credentials confidential and avoid sharing them with anyone. You can update your password anytime in settings. Enjoy safe and seamless access to your account.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
                ) {
                    Text("CONTINUE", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}