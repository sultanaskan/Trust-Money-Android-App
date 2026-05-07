package com.wnapp.trustmoney.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.model.TransactionType

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoneyScreen(
    navController: NavController,
    initialAmount: String? = "0"
) {
    val context = LocalContext.current
    val brandGreen = Color(0xFF00C853)
    val lightBackground = Color(0xFFFAFAFA)
    val textBlack = Color(0xFF000000)
    val textGray = Color(0xFF757575)

    var amount by remember { mutableStateOf(initialAmount ?: "0") }
    val mc = MyCurrency(context)

    // রিসোর্স থেকে স্ট্রিং গেট করা
    val strAddBalance = stringResource(id = R.string.add_balance)
    val strTitle = stringResource(id = R.string.add_balance_your_wallet)
    val strSubTitle = stringResource(id = R.string.fast_and_easy_to_add_balance)
    val strContinue = stringResource(id = R.string.continue_btn)

    val usdToBdtRate = 220
    val exchangeRate = ((usdToBdtRate * 1) / (mc.getRateInUsd()?.toDouble() ?: 0.0))
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
                            tint = textBlack
                        )
                    }
                },
                title = {
                    Text(
                        text = strAddBalance,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textBlack
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
                    text = strTitle,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textBlack
                    )
                )
                Text(
                    text = strSubTitle,
                    color = textGray,
                    fontSize = 14.sp
                )
            }

            CurrencyInputCard(
                currencyName = "${mc.getCountryName()} ",
                amount = amount,
                onAmountChange = {
                    if (it.all { char -> char.isDigit() || char == '.' }) {
                        amount = it
                    }
                },
                icon = Icons.Default.AccountBalanceWallet
            )

            RateConverterCard(mc, String.format("%.2f", exchangeRate))

            CurrencyDetailsSection(
                bdtValue = String.format("%.2f", bdtAmount),
                intensiveValue = String.format("%.2f", totalWithIntensive)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if ((amount.toDoubleOrNull() ?: 0.0) > 0) {
                        navController.navigate(Screen.MethodSelection.passAmountAndTransactionType(amount,TransactionType.deposit.toString()))
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = strContinue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun CurrencyInputCard(
    currencyName: String,
    amount: String,
    onAmountChange: (String) -> Unit, // এটি আপনার ভিউমডেল বা স্ক্রিন থেকে আসবে
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
                // BasicTextField এ onValueChange অবশ্যই থাকতে হবে
                BasicTextField(
                    value = amount,
                    onValueChange = onAmountChange, // এখানে error টি হতে পারে যদি প্যারামিটার নাম ভুল থাকে
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        }
    }
}
@Composable
fun RateConverterCard(mc: MyCurrency, exchangeRate: String) {
    val todaysRateLabel = stringResource(id = R.string.todays_rate)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Row লজিক অপরিবর্তিত থাকবে) ...

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "$todaysRateLabel : 1 ${mc.getCountryName()} ${mc.getCurrencyName()} $exchangeRate BDT",
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
    val strDetails = stringResource(id = R.string.currency_details)
    val strBdtLabel = stringResource(id = R.string.bdt_label)
    val strIncentiveLabel = stringResource(id = R.string.total_with_incentive)
    val strTotalReceived = stringResource(id = R.string.total_received_bdt)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = strDetails,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            DetailItem(label = strBdtLabel, value = bdtValue)
            Spacer(modifier = Modifier.height(8.dp))
            DetailItem(label = strIncentiveLabel, value = intensiveValue)

            Spacer(modifier = Modifier.height(12.dp))

            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "$strTotalReceived :",
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
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
            Text(
                text = "BDT",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi= true)
@Composable
fun AddMoneyScreenPreview(){
    val navController = rememberNavController()
    MaterialTheme {
        AddMoneyScreen(navController = navController)
    }
}