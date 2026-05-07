package com.wnapp.trustmoney.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.R // আপনার রিসোর্স পাথ

/**
 * হোয়াটসঅ্যাপ ওপেন করার হেল্পার ফাংশন
 */
fun openWhatsApp(context: Context, phoneNumber: String) {
    val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setPackage("com.whatsapp")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // হোয়াটসঅ্যাপ ইন্সটল না থাকলে ব্রাউজারে ওপেন হবে
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(navController: NavController) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val supportNumber = "+8801XXXXXXXXX" // এখানে আপনার আসল হোয়াটসঅ্যাপ নাম্বার দিন (কান্ট্রি কোড সহ)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("সাপোর্ট ২৪/৭ পরিষেবা", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = if (isDark) Color.White else Color.Black,
                    navigationIconContentColor = if (isDark) Color.White else Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "প্রশ্ন আছে?",
                color = Color.Gray,
                fontSize = 14.sp
            )

            // হোয়াটসঅ্যাপ সাপোর্ট কার্ড
            SupportItemCard(
                title = "২৪/৭ হোয়াটসঅ্যাপ কল/চ্যাট",
                description = "আমাদের মেসেজ করুন। সাধারণত এক মিনিটেরও কম সময়ের মধ্যে উত্তর দেওয়া হয়।",
                iconRes = Icons.Default.Whatsapp, // নিশ্চিত করুন এই আইকনটি drawable ফোল্ডারে আছে
                iconColor = Color(0xFF25D366),
                isDark = isDark
            ) {
                openWhatsApp(context, supportNumber)
            }

            // সাধারণ কল সাপোর্ট কার্ড
            SupportItemCard(
                title = "সরাসরি কল করুন",
                description = "যেকোনো জরুরি প্রয়োজনে আমাদের হটলাইনে কল করুন।",
                iconRes = Icons.Default.Call, // নিশ্চিত করুন এই আইকনটি drawable ফোল্ডারে আছে
                iconColor = Color(0xFF008346),
                isDark = isDark
            ) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$supportNumber"))
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun SupportItemCard(
    title: String,
    description: String,
    iconRes: ImageVector,
    iconColor: Color,
    isDark: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF252525) else Color(0xFFF7F7F7)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconRes,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isDark) Color.White else Color.Black
                )
                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}