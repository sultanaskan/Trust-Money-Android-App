package com.wnapp.trustmoney.ui.transaction

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.model.TransactionType
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.TrustFeature // Assuming this is moved to a common component file

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileRechargeScreen(
    navController: NavController,
    initialAmount: String? = ""
) {
    // Brand Color Palette (Consistent with your SendMoney design)
    val brandGreen = Color(0xFF1B5E20)
    val lightBackground = Color(0xFFF4F7FA)
    val textBlack = Color(0xFF1A1C1E)
    val textGray = Color(0xFF6C757D)
    val cardBorder = Color(0xFFE9ECEF)

    val context = LocalContext.current
    var amount by remember { mutableStateOf(initialAmount ?: "0") }
    val mc = MyCurrency(context)

    // Conversion Logic
    val usdToBdtRate = 120
    val exchangeRate = ((usdToBdtRate * 1) / (mc.getRateInUsd()?.toDouble() ?: 1.0))
    val bdtAmount by remember {
        derivedStateOf {
            val input = amount.toDoubleOrNull() ?: 0.0
            input * exchangeRate
        }
    }

    MaterialTheme(colorScheme = lightColorScheme(
        primary = brandGreen,
        background = lightBackground,
        surface = Color.White
    )) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, null, modifier = Modifier.size(28.dp), tint = textBlack)
                        }
                    },
                    title = {
                        Text("Mobile Recharge", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textBlack)
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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon Header
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = brandGreen.copy(alpha = 0.1f)
                ) {
                    Icon(
                        Icons.Default.Smartphone,
                        contentDescription = null,
                        tint = brandGreen,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Recharge Amount", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = textBlack)
                Text("Enter the amount you want to top up.", color = textGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(32.dp))

                // AMOUNT CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Your Currency (${mc.getCurrencyName()})", color = textGray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Amount Input
                        BasicTextField(
                            value = amount,
                            onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                            textStyle = TextStyle(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = brandGreen
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = cardBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("BDT Receipt Amount", color = textGray, fontSize = 14.sp)
                            Text(
                                "৳ ${String.format("%.2f", bdtAmount)}",
                                color = textBlack,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Info Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, null, tint = textGray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Recharge will be processed instantly to your local operator.",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))

                // CTA Button
                Button(
                    onClick = {
                        val finalAmount = amount.toDoubleOrNull() ?: 0.0
                        if (finalAmount > 0) {
                            navController.navigate(
                                Screen.MethodSelection.passAmountAndTransactionType(
                                    amount,
                                    TransactionType.recharge.toString()
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = brandGreen),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        "Continue to Operators",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}