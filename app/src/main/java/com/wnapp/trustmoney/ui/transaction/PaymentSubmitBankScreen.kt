package com.wnapp.trustmoney.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// আপনার তৈরি করা হেল্পার কম্পোনেন্টটির ইমপোর্ট (প্যাকেজ নাম আপনার প্রজেক্ট অনুযায়ী চেক করে নিন)
import com.wnapp.trustmoney.ui.transaction.paymentsubmitcomponents.AmountDisplayBox



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSubmitBankScreen(
    navController: NavController,
    amount: String
) {
    val bankBlue = Color(0xFF0054A6)
    val successGreen = Color(0xFF00C853)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bank Payment", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
                Text("alrajhi bank", color = bankBlue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                AmountDisplayBox(amount)
            }

            // ব্যাংক ডিটেইলস এবং আপলোড সেকশন
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(successGreen, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(24.dp)
            ) {
                Text("Payment Document Upload", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bank Details", fontWeight = FontWeight.Bold, color = Color.Black)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Account Holder: ISTEYAK AHMAD", color = Color.DarkGray, fontSize = 14.sp)
                        Text("Account Number: 539000010006", color = Color.DarkGray, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clickable { /* ইমেজ পিকার লজিক */ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = successGreen, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("ট্রান্সফার স্লিপ আপলোড করুন", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* Submit Logic */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("জমা দিন", color = successGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}





@Preview(showBackground = true, showSystemUi= true)
@Composable
fun PaymentSubmitBankScreenPreview(){
    val navController = rememberNavController()
    val amount: String =""
    MaterialTheme {
        PaymentSubmitBankScreen(navController = navController, amount)
    }
}