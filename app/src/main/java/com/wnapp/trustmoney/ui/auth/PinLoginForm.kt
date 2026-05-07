package com.wnapp.trustmoney.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark

@Composable
fun PinLoginForm( onSuccess: () -> Unit) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // .value পদ্ধতি ব্যবহার করা হয়েছে যাতে 'by' ডেলিগেট এরর না আসে
    val pinState = remember { mutableStateOf<String>("") }
    val isPinSet = remember { sessionManager.isPinSet() }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isPinSet) "Welcome Back!" else "Secure Your App",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TBL_Green_Dark
        )

        Text(
            text = if (isPinSet) "Enter 4-digit PIN to login" else "Set a 4-digit PIN for quick access",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        // PIN Input Field
        OutlinedTextField(
            value = pinState.value,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    pinState.value = it
                }
            },
            label = { Text("Enter PIN", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = TBL_Green_Dark) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = TBL_Green_Dark
            )
        )

        // Action Button
        Button(
            onClick = {
                val enteredPin = pinState.value
                if (enteredPin.length == 4) {
                    if (!isPinSet) {
                        // প্রথমবার হলে পিন সেট করবে
                        sessionManager.savePin(enteredPin)
                        Toast.makeText(context, "PIN Set Successfully", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    } else {
                        // সেট করা থাকলে পিন যাচাই করবে
                        if (enteredPin == sessionManager.getPin()) {
                            onSuccess()
                        } else {
                            Toast.makeText(context, "Incorrect PIN!", Toast.LENGTH_SHORT).show()
                            pinState.value = "" // পিন রিসেট
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter 4 digits", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (isPinSet) "Login" else "Set PIN",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // পিন ভুলে গেলে বা ইমেইল দিয়ে লগইন করতে চাইলে
        TextButton(onClick = { /* Nav to Login Screen if needed */ }) {
            Text("Switch to Password Login", color = TBL_Green_Dark)
        }
    }
}