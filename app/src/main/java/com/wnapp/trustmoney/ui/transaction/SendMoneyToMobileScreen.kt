package com.wnapp.trustmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.TransactionType
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.transaction.paymentsubmitcomponents.AmountDisplayBox
import com.wnapp.trustmoney.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyToMobileScreen(
    navController: NavController,
    amount: String,
    paymentMethodId: String,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current

    // Explicit Color Definitions
    val brandGreen = Color(0xFF1B5E20)
    val surfaceLight = Color(0xFFF8F9FA)
    val pureWhite = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF212121)
    val textSecondary = Color(0xFF757575)

    val mc = MyCurrency(context)
    val sm = SessionManager(context)
    val currencyName = mc.getCurrencyName()

    val userId = remember { sm.getUserId() }
    val savedPin = remember { sm.getPin() } // Retrieve the saved PIN from SessionManager

    var recipientNumber by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }

    // PIN Dialog States
    var showPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }

    val currentMethod = viewModel.paymentMethod
    val idAsInt = paymentMethodId.toIntOrNull()

    LaunchedEffect(idAsInt) {
        if (idAsInt != null) {
            viewModel.getPaymentMethod(idAsInt)
        }
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = brandGreen,
            onPrimary = pureWhite,
            surface = pureWhite,
            onSurface = textPrimary,
            background = surfaceLight
        )
    ) {
        // --- 1. PIN CONFIRMATION DIALOG ---
        if (showPinDialog) {
            AlertDialog(
                onDismissRequest = { showPinDialog = false; enteredPin = "" },
                title = { Text("Enter PIN", fontWeight = FontWeight.Bold, color = textPrimary) },
                text = {
                    Column {
                        Text("Confirm your transaction by entering your 4-digit PIN.", color = textSecondary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = enteredPin,
                            onValueChange = { if (it.length <= 4) enteredPin = it },
                            label = { Text("Secret PIN") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = brandGreen) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = brandGreen,
                                unfocusedBorderColor = textSecondary.copy(alpha = 0.3f)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (enteredPin == savedPin) {
                                showPinDialog = false
                                viewModel.submitMobileRequest(
                                    userId = userId,
                                    method = currentMethod?.methodType ?: "mobile",
                                    amount = amount,
                                    type = TransactionType.withdraw,
                                    trxId = recipientNumber,
                                    context = context
                                )
                                enteredPin = ""
                            } else {
                                Toast.makeText(context, "ভুল পিন দিয়েছেন", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = brandGreen)
                    ) {
                        Text("Confirm", color = pureWhite)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPinDialog = false; enteredPin = "" }) {
                        Text("Cancel", color = textSecondary)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = pureWhite
            )
        }

        // --- 2. SUCCESS DIALOG ---
        if (viewModel.isRequestSuccessful) {
            AlertDialog(
                onDismissRequest = { viewModel.isRequestSuccessful = false; navController.navigate(Screen.Home.route) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.isRequestSuccessful = false; navController.popBackStack(Screen.Home.route, false) },
                        colors = ButtonDefaults.buttonColors(containerColor = brandGreen)
                    ) { Text("ঠিক আছে", color = pureWhite) }
                },
                title = { Text("সফল হয়েছে", fontWeight = FontWeight.Bold, color = textPrimary) },
                text = { Text("আপনার সেন্ড মানি রিকোয়েস্টটি সফলভাবে গ্রহণ করা হয়েছে।", color = textPrimary) },
                shape = RoundedCornerShape(20.dp),
                containerColor = pureWhite
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Confirm Transfer", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, "Back", modifier = Modifier.size(28.dp), tint = textPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = pureWhite)
                )
            },
            containerColor = surfaceLight
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when {
                    viewModel.isLoading && currentMethod == null -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = brandGreen)
                    }
                    currentMethod != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Transaction Summary Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = brandGreen)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = currentMethod.bankLogoUrl,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp).clip(CircleShape).background(pureWhite).padding(4.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text = currentMethod.providerName ?: "Transfer", color = pureWhite, fontWeight = FontWeight.Medium)
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    AmountDisplayBox(amount, currencyName)
                                    Text(text = "Sending Total", color = pureWhite.copy(alpha = 0.7f), fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(text = "Recipient Information", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, color = textPrimary)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Name Input
                            OutlinedTextField(
                                value = recipientName,
                                onValueChange = { recipientName = it },
                                label = { Text("Recipient Full Name") },
                                placeholder = { Text("Enter full name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.AccountCircle, null, tint = brandGreen) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = brandGreen,
                                    unfocusedBorderColor = textSecondary.copy(alpha = 0.3f),
                                    focusedTextColor = textPrimary,
                                    unfocusedTextColor = textPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Number Input
                            OutlinedTextField(
                                value = recipientNumber,
                                onValueChange = { if (it.length <= 11) recipientNumber = it },
                                label = { Text("${currentMethod.providerName} Number") },
                                placeholder = { Text("01XXXXXXXXX") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.PhoneIphone, null, tint = brandGreen) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = brandGreen,
                                    unfocusedBorderColor = textSecondary.copy(alpha = 0.3f),
                                    focusedTextColor = textPrimary,
                                    unfocusedTextColor = textPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Security Badge
                            Row(
                                modifier = Modifier.fillMaxWidth().background(brandGreen.copy(alpha = 0.05f), RoundedCornerShape(12.dp)).padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Security, null, tint = brandGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Secure end-to-end encrypted transfer", fontSize = 12.sp, color = brandGreen)
                            }

                            Spacer(modifier = Modifier.height(40.dp))

                            // SUBMIT BUTTON (Trigger Dialog)
                            Button(
                                onClick = {
                                    if (recipientNumber.length >= 11) {
                                        showPinDialog = true // Open the PIN dialog instead of immediate submit
                                    } else {
                                        Toast.makeText(context, "সঠিক মোবাইল নম্বর দিন", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(58.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = brandGreen, contentColor = pureWhite),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = pureWhite, strokeWidth = 2.dp)
                                } else {
                                    Text(text = "টাকা পাঠান", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    else -> { Text(text = "ডাটা লোড সম্ভব হয়নি", modifier = Modifier.align(Alignment.Center), color = textPrimary) }
                }
            }
        }
    }
}