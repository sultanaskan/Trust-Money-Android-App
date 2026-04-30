package com.wnapp.trustmoney.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// আপনার তৈরি করা হেল্পার কম্পোনেন্টগুলোর ইমপোর্ট
import com.wnapp.trustmoney.ui.transaction.paymentsubmitcomponents.AmountDisplayBox
import com.wnapp.trustmoney.ui.transaction.paymentsubmitcomponents.InstructionItem



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSubmitMobileScreen(
    navController: NavController,
    amount: String
) {
    var transactionId by remember { mutableStateOf("") }
    val brandColor = Color(0xFFD12053) // bKash টাইপ কালার
    val successGreen = Color(0xFF00C853)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mobile Payment", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF0F4F7))
        ) {
            // হেডার সেকশন
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("bKash", color = brandColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                AmountDisplayBox(amount)
            }

            // ইনপুট এবং নির্দেশনাবলী সেকশন
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(successGreen, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(24.dp)
            ) {
                Text("ট্রানজেকশন আইডি দিন", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = transactionId,
                    onValueChange = { transactionId = it },
                    placeholder = { Text("ট্রানজেকশন আইডি এখানে লিখুন") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("নির্দেশনাবলী:", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                InstructionItem("bKash App অথবা *247# মেনুতে যান।")
                InstructionItem("\"Send Money\" অপশনটি বেছে নিন।")
                InstructionItem("প্রাপক নম্বর: 01712377332")
                InstructionItem("পরিমাণ: SAR $amount")

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* Submit Logic */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("নিশ্চিত করুন", color = successGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}





@Preview(showBackground = true, showSystemUi= true)
@Composable
fun PaymentSubmitMobileScreenPreview(){
    val navController = rememberNavController()
    val amount: String =""
    MaterialTheme {
        PaymentSubmitMobileScreen(navController = navController, amount)
    }
}