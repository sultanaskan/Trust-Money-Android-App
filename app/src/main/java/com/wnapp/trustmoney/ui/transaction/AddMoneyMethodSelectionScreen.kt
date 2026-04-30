package com.wnapp.trustmoney.ui.transaction

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
import com.wnapp.trustmoney.viewmodel.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoneyMethodSelectionScreen(
    navController: NavController,
    amount: String = ""
) {
    val context = LocalContext.current
    val viewModel: AppViewModel = viewModel(
        factory = AppViewModelFactory(context.applicationContext as Application)
    )

    val brandGreen = Color(0xFF004D40)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Mobile Banking", "Banking")

    val allMethods by viewModel.paymentMethods
    val isLoading by viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.getPaymentMethods()
    }

    val filteredMethods = remember(selectedTabIndex, allMethods) {
        if (selectedTabIndex == 0) {
            allMethods.filter { it.methodType.lowercase() == "mobile" }
        } else {
            allMethods.filter { it.methodType.lowercase() == "banking" }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Money", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("dashboard") }) {
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
                        Text(text = "Selected Amount", fontSize = 12.sp, color = Color.Gray)
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
                    Text(
                        text = "Select your preferred ${tabs[selectedTabIndex]}",
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
                            Text("No payment methods found", color = Color.Gray)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(filteredMethods) { method ->
                                // এই ফাংশনটি নিচে ডিফাইন করা হয়েছে
                                PaymentMethodGridItem(method, brandGreen) {
                                    if(method.methodType == "mobile"){
                                        navController.navigate(Screen.PaymentSubmitMobile.passAmount(amount))
                                    }else{
                                        navController.navigate(Screen.PaymentSubmitBank.passAmount(amount))
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

// এই ফাংশনটি আপনার ফাইলে থাকা নিশ্চিত করুন
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
            text = method.providerName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            maxLines = 1
        )
    }
}




@Preview(showBackground = true, showSystemUi= true)
@Composable
fun AddMoneyMethodSelectionScreenPreview(){
    val navController = rememberNavController()
    val amount: String =""
    MaterialTheme {
        AddMoneyMethodSelectionScreen(navController = navController, amount)
    }
}