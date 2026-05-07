package com.wnapp.trustmoney.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.wnapp.trustmoney.data.model.PaymentMethodItem
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.viewmodel.AppViewModel
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodSelectionScreen(
    navController: NavController,
    amount: String = "",
    transactionType: String,
) {1
    val context = LocalContext.current
    val viewModel: AppViewModel = viewModel(
        factory = AppViewModel.AppViewModelFactory(context.applicationContext as Application)
    )

    val brandGreen = Color(0xFF004D40)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // রিসোর্স থেকে স্ট্রিং গেট করা
    val strMobileBanking = stringResource(id = R.string.mobile_banking)
    val strBanking = stringResource(id = R.string.banking)
    val tabs = listOf(strMobileBanking, strBanking)

    val allMethods by viewModel.paymentMethods
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.getPaymentMethods()
    }

    val filteredMethods = remember(selectedTabIndex, allMethods) {
        if (selectedTabIndex == 0) {
            allMethods.filter { it.methodType?.lowercase() == "mobile" }
        } else {
            allMethods.filter { it.methodType?.lowercase() == "banking" }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.add_money), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                        Icon(Icons.Default.Home, "Home", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = brandGreen)
            )
        },
        bottomBar = {
            if (amount.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .navigationBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.selected_amount), fontSize = 12.sp, color = Color.Gray)
                        Text(
                            text = "৳ $amount",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = brandGreen
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = brandGreen,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = brandGreen
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = brandGreen)
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    // %1$s ফরম্যাট ব্যবহার করে ডাইনামিক স্ট্রিং
                    Text(
                        text = stringResource(id = R.string.select_preferred_method, tabs[selectedTabIndex]),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FBF8), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (filteredMethods.isEmpty()) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(stringResource(id = R.string.no_methods_found), color = Color.Gray)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(filteredMethods) { method ->
                                PaymentMethodGridItem(method, brandGreen) {
                                    if( transactionType == TransactionType.deposit.toString()) {
                                        if (method.methodType == "mobile") {
                                            navController.navigate(
                                                Screen.PaymentSubmitMobile.passAmountAndProvider(
                                                    amount,
                                                    method.id.toString()
                                                )
                                            )
                                        } else {
                                            navController.navigate(
                                                Screen.PaymentSubmitBank.passAmountAndProvider(
                                                    amount,
                                                    method.id.toString()
                                                )
                                            )
                                        }
                                    }else if(transactionType == TransactionType.withdraw.toString()){
                                        if(method.methodType == "mobile") {
                                            navController.navigate(
                                                Screen.SendMoneyToMobile.passAmountAndMethod(
                                                    amount,
                                                    method.id.toString()
                                                )
                                            )
                                        }else{
                                            navController.navigate(
                                                Screen.SendMoneyToBank.passAmountAndMethod(
                                                    amount,
                                                    method.id.toString()
                                                )
                                            )
                                        }
                                    }else if(transactionType == TransactionType.recharge.toString()){
                                        navController.navigate(
                                            Screen.MobileRechargeConfirmation.passAmountAndMethod(
                                                amount,
                                                method.id.toString()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun PaymentMethodGridItem(
    method: PaymentMethodItem,
    brandColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = method.bankLogoUrl,
                    contentDescription = method.providerName,
                    modifier = Modifier.size(45.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = method?.providerName ?: "",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            maxLines = 1
        )
    }
}


@Preview(showBackground = true, showSystemUi= true)
@Composable
fun MethodSelectionScreenPreview(){
    val navController = rememberNavController()
    val amount: String =""
    MaterialTheme {
        MethodSelectionScreen(navController = navController, amount, TransactionType.deposit.toString())
    }
}