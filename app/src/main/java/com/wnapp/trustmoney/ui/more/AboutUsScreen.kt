package com.wnapp.trustmoney.ui.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Us", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ১. লোগো সেকশন
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                // আপনার অ্যাপের লোগো এখানে বসান
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Logo",
                    tint = Color(0xFF2E7D32), // Brand Green
                    modifier = Modifier.padding(20.dp)
                )
            }

            Text(
                text = "Trust Bank PLC.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFF2E7D32)
            )

            Text(
                text = "Version 1.0.2",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // ২. আমাদের সম্পর্কে বর্ণনা
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "আমাদের লক্ষ্য",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ট্রাস্ট ব্যাংক পিএলসি একটি আধুনিক ডিজিটাল ব্যাংকিং সেবা প্রদানকারী প্রতিষ্ঠান। আমরা গ্রাহকদের সর্বোচ্চ নিরাপত্তা এবং দ্রুততম লেনদেন নিশ্চিত করতে প্রতিশ্রুতিবদ্ধ। আমাদের স্মার্ট অ্যাপের মাধ্যমে আপনি যেকোনো সময়, যেকোনো স্থান থেকে ব্যাংকিং সুবিধা গ্রহণ করতে পারেন।",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify,
                        color = Color.DarkGray
                    )
                }
            }

            // ৩. কন্টাক্ট ইনফরমেশন
            Text(
                text = "যোগাযোগ করুন",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                fontSize = 16.sp
            )

            ContactInfoItem(Icons.Default.LocationOn, "হেড অফিস", "উত্তরা, ঢাকা, বাংলাদেশ")
            ContactInfoItem(Icons.Default.Phone, "হেল্পলাইন", "+880 1234 567890")
            ContactInfoItem(Icons.Default.Email, "ইমেইল", "support@trustbank.com")

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "© 2026 Trust Bank PLC. All Rights Reserved.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
    AboutUsScreen(navController = rememberNavController())
}