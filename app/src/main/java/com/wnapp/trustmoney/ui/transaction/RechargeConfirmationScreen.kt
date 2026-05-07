package com.wnapp.trustmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun MobileRechargeConfirmationScreen(
    navController: NavController,
    amount: String,
    paymentMethodId: String,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current

    // Brand Palette
    val rechargeBlue = Color(0xFF0C7700) // Professional Telecom Blue
    val surfaceLight = Color(0xFFF8F9FA)
    val pureWhite = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF212121)
    val textSecondary = Color(0xFF757575)

    val mc = MyCurrency(context)
    val sm = SessionManager(context)
    val currencyName = mc.getCurrencyName()
    val userId = remember { sm.getUserId() }
    val savedPin = remember { sm.getPin() }

    // Form States
    var receiverNumber by remember { mutableStateOf("") }
    var selectedRelation by remember { mutableStateOf("Self") }
    val relations = listOf("Self", "Father", "Mother", "Brother", "Sister", "Wife", "Other")

    // Dialog States
    var showPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }

    val currentMethod = viewModel.paymentMethod
    val idAsInt = paymentMethodId.toIntOrNull()

    LaunchedEffect(idAsInt) {
        if (idAsInt != null) viewModel.getPaymentMethod(idAsInt)
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = rechargeBlue,
            background = surfaceLight,
            surface = pureWhite
        )
    ) {
        // --- PIN DIALOG ---
        if (showPinDialog) {
            AlertDialog(
                onDismissRequest = { showPinDialog = false; enteredPin = "" },
                title = { Text("Confirm Recharge", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Enter your 4-digit security PIN to finalize the top-up.", color = textSecondary)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = enteredPin,
                            onValueChange = { if (it.length <= 4) enteredPin = it },
                            label = { Text("Security PIN") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = rechargeBlue) }
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
                                    method = "Mobile Recharge",
                                    amount = amount,
                                    type = TransactionType.recharge,
                                    trxId = receiverNumber,
                                    context = context
                                )
                                enteredPin = ""
                            } else {
                                Toast.makeText(context, "ভুল পিন দিয়েছেন", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) { Text("Confirm", color = pureWhite) }
                },
                shape = RoundedCornerShape(24.dp)
            )
        }

        // --- SUCCESS DIALOG ---
        if (viewModel.isRequestSuccessful) {
            AlertDialog(
                onDismissRequest = { viewModel.isRequestSuccessful = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.isRequestSuccessful = false
                        navController.popBackStack(Screen.Home.route, false)
                    }) { Text("ঠিক আছে") }
                },
                title = { Text("সফল হয়েছে", fontWeight = FontWeight.Bold) },
                text = { Text("আপনার মোবাইল রিচার্জ রিকোয়েস্টটি সফলভাবে পাঠানো হয়েছে।") },
                shape = RoundedCornerShape(20.dp)
            )
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Recharge Confirmation", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, "Back", modifier = Modifier.size(28.dp))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = pureWhite)
                )
            },
            containerColor = surfaceLight
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Visual: Operator and Amount
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(pureWhite, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .padding(bottom = 32.dp, top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = rechargeBlue.copy(alpha = 0.1f)
                        ) {
                            if (currentMethod != null) {
                                AsyncImage(
                                    model = currentMethod.bankLogoUrl,
                                    contentDescription = null,
                                    modifier = Modifier.padding(16.dp).clip(CircleShape),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Icon(Icons.Default.Smartphone, null, tint = rechargeBlue, modifier = Modifier.padding(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(currentMethod?.providerName ?: "Operator", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        AmountDisplayBox(amount, currencyName)
                    }
                }

                // Form Section
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Recharge Details", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = textPrimary)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Receiver Number Input
                    OutlinedTextField(
                        value = receiverNumber,
                        onValueChange = { if (it.length <= 11) receiverNumber = it },
                        label = { Text("Receiver Mobile Number") },
                        placeholder = { Text("01XXXXXXXXX") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Default.PhoneIphone, null, tint = rechargeBlue) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Relation Selection (Friendly Environment UI)
                    Text("Select Relation", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textSecondary)
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(relations) { relation ->
                            FilterChip(
                                selected = selectedRelation == relation,
                                onClick = { selectedRelation = relation },
                                label = { Text(relation) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = rechargeBlue,
                                    selectedLabelColor = pureWhite
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Instant Success Badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rechargeBlue.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.FlashOn, null, tint = rechargeBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Instant recharge after PIN confirmation", fontSize = 12.sp, color = rechargeBlue)
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (receiverNumber.length >= 11) {
                                showPinDialog = true
                            } else {
                                Toast.makeText(context, "সঠিক মোবাইল নম্বর দিন", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !viewModel.isLoading
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = pureWhite)
                        } else {
                            Text("Recharge Now", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}