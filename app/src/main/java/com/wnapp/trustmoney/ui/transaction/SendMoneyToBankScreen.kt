package com.wnapp.trustmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun SendMoneyToBankScreen(
    navController: NavController,
    amount: String,
    paymentMethodId: String,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current

    // UI Palette
    val bankNavy = Color(0xFF1B6000)
    val backgroundGray = Color(0xFFF4F7FA)
    val textPrimary = Color(0xFF1A1C1E)
    val textSecondary = Color(0xFF6C757D)
    val pureWhite = Color(0xFFFFFFFF)

    val mc = MyCurrency(context)
    val sm = SessionManager(context)

    val userId = remember { sm.getUserId() }
    val savedPin = remember { sm.getPin() }
    val currencyName = mc.getCurrencyName()

    // Form States
    var accountNumber by remember { mutableStateOf("") }
    var accountName by remember { mutableStateOf("") }
    var branchName by remember { mutableStateOf("") }

    // Dialog States
    var showPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }

    // Fetch Method Data
    val currentMethod = viewModel.paymentMethod
    val idAsInt = paymentMethodId.toIntOrNull()

    LaunchedEffect(idAsInt) {
        if (idAsInt != null) {
            viewModel.getPaymentMethod(idAsInt)
        }
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = bankNavy,
            background = backgroundGray,
            surface = pureWhite
        )
    ) {
        // --- 1. PIN VERIFICATION DIALOG ---
        if (showPinDialog) {
            AlertDialog(
                onDismissRequest = { showPinDialog = false; enteredPin = "" },
                title = { Text("Confirm Transfer", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Enter your 4-digit security PIN to authorize this transfer.", color = textSecondary)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = enteredPin,
                            onValueChange = { if (it.length <= 4) enteredPin = it },
                            label = { Text("Enter PIN") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = bankNavy) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = bankNavy)
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
                                    method = currentMethod?.providerName ?: "Bank Transfer",
                                    amount = amount,
                                    type = TransactionType.withdraw,
                                    context = context,
                                    trxId = accountNumber
                                )
                                enteredPin = ""
                            } else {
                                Toast.makeText(context, "ভুল পিন দিয়েছেন", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = bankNavy)
                    ) { Text("Authorize", color = pureWhite) }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = pureWhite
            )
        }

        // --- 2. SUCCESS DIALOG ---
        if (viewModel.isRequestSuccessful) {
            AlertDialog(
                onDismissRequest = { viewModel.isRequestSuccessful = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.isRequestSuccessful = false
                        navController.popBackStack(Screen.Home.route, false)
                    }) { Text("ঠিক আছে") }
                },
                title = { Text("সফল হয়েছে", fontWeight = FontWeight.Bold) },
                text = { Text("আপনার ব্যাংক ট্রান্সফার রিকোয়েস্টটি সফলভাবে পাঠানো হয়েছে।") },
                shape = RoundedCornerShape(20.dp)
            )
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Bank Transfer", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, "Back", modifier = Modifier.size(28.dp))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = pureWhite)
                )
            },
            containerColor = backgroundGray
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (viewModel.isLoading && currentMethod == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = bankNavy)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Summary Section with Logo (Updated to match Mobile Screen Style)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = bankNavy)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Bank Logo
                                    AsyncImage(
                                        model = currentMethod?.bankLogoUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(pureWhite)
                                            .padding(4.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    // Bank Name
                                    Text(
                                        text = currentMethod?.providerName ?: "Bank Transfer",
                                        color = pureWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                AmountDisplayBox(amount, currencyName)
                                Text("Amount to Send", color = pureWhite.copy(alpha = 0.8f), fontSize = 13.sp)
                            }
                        }

                        // Input Section
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                "Bank Account Details",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = textPrimary,
                                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                            )

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = pureWhite,
                                shadowElevation = 2.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    // Account Number
                                    OutlinedTextField(
                                        value = accountNumber,
                                        onValueChange = { accountNumber = it },
                                        label = { Text("Account Number") },
                                        modifier = Modifier.fillMaxWidth(),
                                        leadingIcon = { Icon(Icons.Default.CreditCard, null, tint = bankNavy) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = bankNavy,
                                            unfocusedBorderColor = textSecondary.copy(alpha = 0.2f)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Account Holder Name
                                    OutlinedTextField(
                                        value = accountName,
                                        onValueChange = { accountName = it },
                                        label = { Text("Account Holder Name") },
                                        modifier = Modifier.fillMaxWidth(),
                                        leadingIcon = { Icon(Icons.Default.Person, null, tint = bankNavy) },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = bankNavy,
                                            unfocusedBorderColor = textSecondary.copy(alpha = 0.2f)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Branch Name Input
                                    OutlinedTextField(
                                        value = branchName,
                                        onValueChange = { branchName = it },
                                        label = { Text("Branch Name") },
                                        placeholder = { Text("e.g. Dhaka Main Branch") },
                                        modifier = Modifier.fillMaxWidth(),
                                        leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = bankNavy) },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = bankNavy,
                                            unfocusedBorderColor = textSecondary.copy(alpha = 0.2f)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Security & Disclaimer
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(bankNavy.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Shield, null, tint = bankNavy, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Funds will be credited to the account within 24 hours.",
                                    fontSize = 12.sp,
                                    color = bankNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Submit Button
                            Button(
                                onClick = {
                                    if (accountNumber.isNotEmpty() && accountName.isNotEmpty() && branchName.isNotEmpty()) {
                                        showPinDialog = true
                                    } else {
                                        Toast.makeText(context, "তথ্যগুলো সম্পূর্ণ ভাবে পূরণ করুন", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(58.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = bankNavy),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = pureWhite)
                                } else {
                                    Text(
                                        "Confirm & Transfer",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = pureWhite
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}