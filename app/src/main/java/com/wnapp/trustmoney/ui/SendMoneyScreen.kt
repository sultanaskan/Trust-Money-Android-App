package com.wnapp.trustmoney.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Send
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

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(
    navController: NavController,
    initialAmount: String? = ""
) {
    // Brand Color Palette
    val brandGreen = Color(0xFF1B5E20) // Deep professional green
    val lightBackground = Color(0xFFF8F9FA)
    val textBlack = Color(0xFF1A1A1A)
    val textGray = Color(0xFF6C757D)
    val cardBorder = Color(0xFFE9ECEF)

    val context = LocalContext.current
    var amount by remember { mutableStateOf(initialAmount ?: "0") }
    val mc = MyCurrency(context)

    // Conversion Logic (Existing functionality)
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
                        Text("Enter Amount to Send", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textBlack)
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Transfer Status/Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(brandGreen, RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Transaction Overview", color = brandGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Send Money", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = textBlack)
                Text("Specify the amount you wish to transfer from your wallet.", color = textGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(32.dp))

                // AMOUNT CARD
                Text("Transfer Amount", color = textBlack, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, cardBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, null, tint = brandGreen, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Amount in ${mc.getCurrencyName()}", color = textGray, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        BasicTextField(
                            value = amount,
                            onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                            textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black, color = textBlack),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = cardBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Estimated BDT", color = textGray, fontSize = 13.sp)
                            Text("৳ ${String.format("%.2f", bdtAmount)}", color = brandGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Features/Trust Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TrustFeature(Icons.Default.Send, "Instant Transfer")
                    TrustFeature(Icons.Default.AccountCircle, "Secure Gateway")
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(20.dp))

                // CTA Button
                Button(
                    onClick = {
                        val finalAmount = amount.toDoubleOrNull() ?: 0.0
                        if (finalAmount > 0) {
                            navController.navigate(
                                Screen.MethodSelection.passAmountAndTransactionType(
                                    amount,
                                    TransactionType.withdraw.toString()
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = brandGreen),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text("Continue to Methods", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TrustFeature(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = Color(0xFF6C757D))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, color = Color(0xFF6C757D))
    }
}