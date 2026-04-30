package com.wnapp.trustmoney.ui.transaction

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController


// স্ক্রিনশটের মতো সেন্টারড টপ বারের জন্য (Experimental)
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyRequestScreen(
    navController: NavController,
    amount: String, // আগের স্ক্রিন থেকে আসা অ্যামাউন্ট
    paymentType: String // "MOBILE" অথবা "BANK"
) {
    val textBlack = Color(0xFF000000)
    val lightBackground = Color(0xFFF0F4F7) // হালকা নীলচে ব্যাকগ্রাউন্ড

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "Back", tint = textBlack)
                    }
                },
                title = { /* টাইটেল খালি রাখতে পারেন স্ক্রিনশটের মতো */ },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBackground)
        ) {
            // কমন টপ সেকশন (লোগো এবং অ্যামাউন্ট ডিসপ্লে)
            PaymentHeader(paymentType = paymentType, amount = amount)

            // কন্ডিশনাল কম্পোনেন্ট
            if (paymentType == "MOBILE") {
                MobileBankingComponent(amount = amount)
            } else {
                BankPaymentComponent(amount = amount)
            }
        }
    }
}



@Composable
fun PaymentHeader(paymentType: String, amount: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // লোগো সেকশন
        if (paymentType == "MOBILE") {
            // bKash লোগো ইমেজ ব্যবহার করুন
            Text("bKash", color = Color(0xFFD12053), fontSize = 32.sp, fontWeight = FontWeight.Bold)
        } else {
            // Al Rajhi Bank লোগো ইমেজ ব্যবহার করুন
            Text("alrajhi bank", color = Color(0xFF0054A6), fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // অ্যামাউন্ট বক্স
        Surface(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "SAR $amount",
                    color = Color(0xFF5E6D82),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Composable
fun MobileBankingComponent(amount: String) {
    var transactionId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00C853), RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(24.dp)
    ) {
        Text("ট্রানজেকশন আইডি দিন", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // ট্রানজেকশন আইডি ইনপুট ফিল্ড
        TextField(
            value = transactionId,
            onValueChange = { transactionId = it },
            placeholder = { Text("ট্রানজেকশন আইডি দিন") },
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

        // নির্দেশনাবলী (Bullet Points)
        InstructionItem("মোবাইল ব্যাংকিং App অথবা মোবাইলে bKash Account অপারেটর এর USSD মেনুতে যান।")
        InstructionItem("\"Send Money\" -এ ক্লিক করুন।")
        InstructionItem("প্রাপক নম্বর হিসেবে এই নম্বরটি লিখুন: 01712377332")
        InstructionItem("টাকার পরিমাণ: SAR $amount")
        // ... বাকি নির্দেশনাবলী ...
    }
}
@Composable
fun InstructionItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // পয়েন্টগুলোর মাঝে গ্যাপ
        verticalAlignment = Alignment.Top
    ) {
        // ১. ছোট সাদা বৃত্তাকার বুলেট পয়েন্ট
        Box(
            modifier = Modifier
                .padding(top = 6.dp) // টেক্সটের সাথে এলাইন করার জন্য
                .size(6.dp)
                .background(Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // ২. নির্দেশনামূলক টেক্সট
        Text(
            text = text,
            style = TextStyle(
                color = Color.White, // ডার্ক মোডেও সাদা থাকবে
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp // লাইনগুলোর মাঝে সুন্দর গ্যাপের জন্য
            )
        )
    }
}


@Composable
fun BankPaymentComponent(amount: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00C853), RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(24.dp)
    ) {
        Text("Payment Document Upload", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // ব্যাংক ডিটেইলস কার্ড
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Bank Details", fontWeight = FontWeight.Bold)
                Text("Account Holder: ISTEYAK AHMAD", color = Color.Gray)
                Text("Account Number: 539000010006", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // আপলোড বক্স
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .clickable { /* ইমেজ পিকার ওপেন হবে */ },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF00C853), modifier = Modifier.size(48.dp))
                Text("Upload Document", color = Color.Gray)
            }
        }
    }
}



@Preview
@Composable
fun MoneyRequestScreenPreview(){
    val navController = rememberNavController()
    val amount: String = "856565"
    val paymentType: String = "laksjdf"
    MoneyRequestScreen(navController, amount, paymentType)

}
