package com.wnapp.trustmoney.ui.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
// এই দুটি ইমপোর্ট 'by' ডেলিগেটের এরর সমাধান করবে
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.BrandGreen

@Composable
fun PinEntryScreen(navController: NavController) {
    val context = LocalContext.current

    // SessionManager কে remember করার জন্য সঠিক সিনট্যাক্স
    val sm = remember { SessionManager(context) }

    // এখানে এখন আর এরর দেখাবে না
    var inputPin by remember { mutableStateOf<String>("") }

    val isFirstTime = remember(inputPin) { !sm.isPinSet() }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isFirstTime) "Set Your 4-Digit PIN" else "Enter Your PIN",
            style = MaterialTheme.typography.headlineSmall,
            color = BrandGreen,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = inputPin,
            onValueChange = {
                // শুধুমাত্র সংখ্যা ইনপুট নেওয়ার ফিল্টার
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    inputPin = it
                }
            },
            label = { Text("Enter 4-Digit PIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (inputPin.length == 4) {
                    if (isFirstTime) {
                        sm.savePin(inputPin)
                        Toast.makeText(context, "PIN Saved Successfully!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        if (inputPin == sm.getPin()) {
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show()
                            inputPin = "" // ভুল পিন দিলে ফিল্ড খালি করে দেওয়া
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Confirm", fontWeight = FontWeight.Bold)
        }
    }
}