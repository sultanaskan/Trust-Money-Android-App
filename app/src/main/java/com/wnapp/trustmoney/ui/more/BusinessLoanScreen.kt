package com.wnapp.trustmoney.ui.more


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wnapp.trustmoney.ui.components.AppTextField

/**
 * সোর্স: Input Form with State Management
 * কনসেপ্ট: Data Collection UI
 * কাজ: ইউজারের ব্যবসার তথ্য নিয়ে লোনের আবেদন গ্রহণ করা।
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessLoanScreen(onBack: () -> Unit) {
    var businessName by remember { mutableStateOf("") }
    var loanAmount by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Business Loan Application") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // ফর্ম বড় হলে স্ক্রোল করার জন্য
        ) {
            Text("Fill up the form to apply for a business loan.")

            AppTextField(value = businessName, onValueChange = { businessName = it }, label = "Business Name")
            AppTextField(value = loanAmount, onValueChange = { loanAmount = it }, label = "Loan Amount (GBP)")
            AppTextField(value = reason, onValueChange = { reason = it }, label = "Reason for Loan")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* আবেদন জমা দেওয়ার লজিক */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Application")
            }
        }
    }
}