package com.wnapp.trustmoney.ui.transaction

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.Package
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.viewmodel.AppViewModel
import com.wnapp.trustmoney.viewmodel.AppViewModelFactory // নিশ্চিত করুন এটি ইমপোর্ট হয়েছে

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageScreen(navController: NavController) {
    val context = LocalContext.current

    // ১. Factory ব্যবহার করে ViewModel ইনিশিয়ালাইজ করা (ক্রাশ রোধের প্রধান উপায়)
    val viewModel: AppViewModel = viewModel(
        factory = AppViewModelFactory(context.applicationContext as Application)
    )

    val mc = remember { MyCurrency(context) }
    val textBlack = Color(0xFF000000)
    val bgGray = Color(0xFFF7F7F7)

    // ২. স্টেটগুলোকে 'by' কিউওয়ার্ড দিয়ে অবজার্ভ করা
    val packages by viewModel.packages
    val isLoading by viewModel.isLoading
    val usdToBdtRate = 122
    val currencyData = CurrencyItem(mc.getCurrencyId(), mc.getCountryName().toString(), mc.getFlagUrl().toString(), mc.getCurrencyName().toString(), mc.getRateInUsd().toString())

    LaunchedEffect(Unit) {
        viewModel.getPackages()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Packages", fontWeight = FontWeight.Bold, color = textBlack) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = textBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgGray)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF00C853))
            } else {
                if (packages.isEmpty()) {
                    Text("No packages available", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(packages) { pkg ->
                            PackageCard(pkg, navController, currencyData, usdToBdtRate)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PackageCard(item: Package, navController: NavController, currencyData: CurrencyItem?, usdToBdtRate: Int) {
    val textBlack = Color(0xFF000000)
    val textGray = Color(0xFF757575)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.packageName, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textBlack))
                IconButton(
                    onClick = { navController.navigate(Screen.AddMoneyMethodSelectionScreen.passAmount(item.price.toString())) },
                    modifier = Modifier.size(40.dp).background(Color(0xFFE8F5E9), CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF2E7D32))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = item.features, color = textGray, fontSize = 15.sp, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // মোট ব্যালেন্স বক্স (Built-in Surface & Column ব্যবহার করে)
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F5F5),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "মোট ব্যালেন্স পাবেন",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = run {
                                val rateInUsd = currencyData?.rateInUsd?.toDoubleOrNull() ?: 1.0
                                val finalRate = if (rateInUsd != 0.0) (1 / rateInUsd) else 1.0
                                "৳${(finalRate * usdToBdtRate * item.price).toInt()}"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                // মোট ডিপোজিট বক্স
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F5F5),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "মোট ডিপোজিট",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${item.price} ${currencyData?.currencyName ?: "USD"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("আজকের রেট", color = textGray, fontSize = 14.sp)
                    Text(
                        text = run {
                            val rateInUsd = currencyData?.rateInUsd?.toDoubleOrNull() ?: 1.0
                            val finalRate = if (rateInUsd != 0.0) (1 / rateInUsd) * usdToBdtRate else 0.0
                            "৳ ${String.format("%.2f", finalRate)} BDT"
                        },
                        fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textBlack
                    )
                }
                Button(
                    onClick = { navController.navigate(Screen.AddMoneyMethodSelectionScreen.passAmount(item.price.toString())) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ডিপোজিট করুন", color = Color.White)
                }
            }
        }
    }
}