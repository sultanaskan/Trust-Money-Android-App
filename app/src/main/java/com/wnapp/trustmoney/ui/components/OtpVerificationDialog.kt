package com.wnapp.trustmoney.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
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
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationDialog(
    onDismiss: () -> Unit,
    onVerify: (String) -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(179) } // ২ মিনিট ৫৯ সেকেন্ড (সেকেন্ডে)

    // টাইমার লজিক
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        confirmButton = {}, // আমরা কাস্টম বাটন নিচে বডিতে ব্যবহার করছি
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Please wait while we verify your email/mobile number",
                    color = TBL_Green_Dark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "An OTP has been sent to your registered mobile or email address. Please enter the OTP below",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Verification Code Header & Timer
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Verification Code", color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(String.format("%02d:%02d", minutes, seconds), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(Icons.Outlined.Refresh, null, modifier = Modifier.size(18.dp).clickable { /* Resend OTP */ })
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // OTP Input Field (কাস্টম বক্সেস)
                OtpInputField(otpCode) {
                    if (it.length <= 6) otpCode = it
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Submit Button
                Button(
                    onClick = { onVerify(otpCode) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Submit", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    )
}

@Composable
fun OtpInputField(code: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = code,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(6) { index ->
                    val char = when {
                        index >= code.length -> ""
                        else -> code[index].toString()
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .border(1.dp, TBL_Green_Dark, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(char, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TBL_Green_Dark)
                    }
                }
            }
        }
    )
}