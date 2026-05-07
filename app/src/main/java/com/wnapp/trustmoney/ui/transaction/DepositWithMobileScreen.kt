package com.wnapp.trustmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.wnapp.trustmoney.ui.transaction.paymentsubmitcomponents.InstructionItem
import com.wnapp.trustmoney.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositWithMobileScreen(
    navController: NavController,
    amount: String,
    paymentMethodId: String,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    val brandColor = Color(0xFFD12053)
    val successGreen = Color(0xFF00C853)
    val mc = MyCurrency(context)
    val sm = SessionManager(context)
    val currencyName = mc.getCurrencyName()

    val userId = remember { sm.getUserId() }
    var inputTrxId by remember { mutableStateOf("") }

    val currentMethod = viewModel.paymentMethod
    val idAsInt = paymentMethodId.toIntOrNull()

    LaunchedEffect(idAsInt) {
        if (idAsInt != null) {
            viewModel.getPaymentMethod(idAsInt)
        }
    }

    // --- Confirmation Dialog Logic ---
    if (viewModel.isRequestSuccessful) {
        AlertDialog(
            onDismissRequest = {
                viewModel.isRequestSuccessful = false
                navController.navigate(Screen.Home.route)
            },
            title = {
                Text(text = "সফল হয়েছে", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "আপনার রিকোয়েস্টটি সফলভাবে সার্ভারে পাঠানো হয়েছে। আমাদের প্রতিনিধি শীঘ্রই এটি যাচাই করবেন।")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.isRequestSuccessful = false
                        navController.popBackStack() // সফল হলে আগের স্ক্রিনে ফিরে যাবে
                    }
                ) {
                    Text("ঠিক আছে", color = successGreen, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mobile Payment", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                viewModel.isLoading && currentMethod == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = brandColor
                    )
                }

                currentMethod != null -> {
                    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F4F7))) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = currentMethod.bankLogoUrl,
                                contentDescription = "Bank Logo",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            AmountDisplayBox(amount, currencyName)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(successGreen, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                                .padding(24.dp)
                        ) {
                            Text("আপনার TrxID/মোবাইল নাম্বার দিন", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = inputTrxId,
                                onValueChange = { inputTrxId = it },
                                placeholder = { Text("ট্রানজেকশন আইডি এখানে লিখুন") },
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
                            Text("নির্দেশনাবলী:", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(12.dp))
                            InstructionItem("${currentMethod.providerName ?: "Mobile"} App থেকে Send Money করুন।")
                            InstructionItem("প্রাপক নম্বর: ${currentMethod.accountNumber ?: ""}")
                            InstructionItem("পরিমাণ: $currencyName $amount")

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                onClick = {
                                    if (inputTrxId.isNotBlank()) {
                                        viewModel.submitMobileRequest(
                                            userId = userId,
                                            method = currentMethod.methodType ?: "mobile",
                                            amount = amount,
                                            type =  TransactionType.deposit,
                                            trxId = inputTrxId,
                                            context = context
                                        )
                                    } else {
                                        Toast.makeText(context, "অনুগ্রহ করে ট্রানজেকশন আইডি দিন", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(55.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = successGreen, strokeWidth = 2.dp)
                                } else {
                                    Text("নিশ্চিত করুন", color = successGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                else -> { Text("ডাটা লোড সম্ভব হয়নি", modifier = Modifier.align(Alignment.Center)) }
            }
        }
    }
}