package com.wnapp.trustmoney.ui.transaction

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.viewmodel.AppViewModel

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBalanceScreen(
    navController: NavController,
    initialAmount: String? = "0"
) {

    val brandGreen = Color(0xFF00C853)
    val lightBackground = Color(0xFFFAFAFA)
    val textBlack = Color(0xFF000000) // ফিক্সড ব্ল্যাক কালার
    val textGray = Color(0xFF757575)  // ফিক্সড গ্রে কালার

    var amount by remember { mutableStateOf(initialAmount ?: "0") }
    val usdToBdtRate = 220
    val foreignToUsd = 100

    val exchangeRate = (1/5) *  usdToBdtRate
    val intensiveRate = 0.025

    val bdtAmount by remember {
        derivedStateOf {
            val input = amount.toDoubleOrNull() ?: 0.0
            input * exchangeRate
        }
    }

    val totalWithIntensive by remember {
        derivedStateOf {
            bdtAmount + (bdtAmount * intensiveRate)
        }
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp),
                            tint = textBlack // আইকন কালার ফিক্সড
                        )
                    }
                },
                title = {
                    Text(
                        "Add Balance",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textBlack // টাইটেল কালার ফিক্সড
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBackground)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "Add Balance Your Wallet",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textBlack // টাইটেল কালার ফিক্সড
                    )
                )
                Text(
                    text = "Fast and easy to add balance",
                    color = textGray, // সাবটাইটেল কালার ফিক্সড
                    fontSize = 14.sp
                )
            }

            CurrencyInputCard(
                currencyName = "Saudi Arabia SAR",
                amount = amount,
                onAmountChange = {
                    if (it.all { char -> char.isDigit() || char == '.' }) {
                        amount = it
                    }
                },
                icon = Icons.Default.AccountBalanceWallet
            )

            RateConverterCard()

            CurrencyDetailsSection(
                bdtValue = String.format("%.2f", bdtAmount),
                intensiveValue = String.format("%.2f", totalWithIntensive)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if ((amount.toDoubleOrNull() ?: 0.0) > 0) {
                        navController.navigate(Screen.AddMoneyMethodSelectionScreen.passAmount(amount))
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // বাটনের টেক্সট সবসময় সাদা
                )
            }
        }
    }
}

@Composable
fun CurrencyInputCard(
    currencyName: String,
    amount: String,
    onAmountChange: (String) -> Unit,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = currencyName,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                BasicTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black // ইনপুট টেক্সট কালার ফিক্সড
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        }
    }
}

@Composable
fun RateConverterCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(24.dp).background(Color(0xFF006C35), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saudi Arabia\n(SAR)",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color.Black // কালার ফিক্সড
                    )
                }

                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = Color.Black // কালার ফিক্সড
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Bangladesh",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Black // কালার ফিক্সড
                    )
                    Box(modifier = Modifier.size(24.dp).background(Color.Red, CircleShape))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Today's Rate : 1 Saudi Arabia SAR 37.4 BDT",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CurrencyDetailsSection(bdtValue: String, intensiveValue: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Currency Details",
                fontWeight = FontWeight.Bold,
                color = Color.Black, // কালার ফিক্সড
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            DetailItem(label = "Bangladeshi Taka (BDT)", value = bdtValue)
            Spacer(modifier = Modifier.height(8.dp))
            DetailItem(label = "Total with Incentive (2.5%)", value = intensiveValue)

            Spacer(modifier = Modifier.height(12.dp))

            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Total Received BDT :",
                        fontWeight = FontWeight.Medium,
                        color = Color.Black // কালার ফিক্সড
                    )
                    Text(
                        text = "$intensiveValue ৳",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9F9F9),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "BDT", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black // কালার ফিক্সড
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi= true)
@Composable
fun AddBalanceScreenPreview(){
    val navController = rememberNavController()
    MaterialTheme {
        AddBalanceScreen(navController = navController)
    }
}