package com.wnapp.trustmoney.ui.transaction.methodselection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.ui.theme.BrandGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundTransferMethodSelectionScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Fund Transfer",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // সরাসরি হোম স্ক্রিনে নেভিগেট করবে
                        navController.navigate("home_route") {
                            popUpTo("home_route") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrandGreen
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(16.dp)
        ) {
            // ১. সিলেক্ট ট্রান্সফার টাইপ কার্ড
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FBF8))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Select your preferred ")
                        withStyle(style = SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                            append("Transfer Type")
                        }
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ২. ট্রান্সফার অপশন লিস্ট
            TransferOptionItem(
                title = "Own A/C Transfer",
                icon = Icons.Default.SyncAlt,
                brandColor = BrandGreen
            ) {
                // নিজস্ব অ্যাকাউন্টে ট্রান্সফার লজিক
            }

            Spacer(modifier = Modifier.height(12.dp))

            TransferOptionItem(
                title = "TBL A/C Transfer",
                icon = Icons.Default.SyncAlt,
                brandColor = BrandGreen
            ) {
                // TBL অ্যাকাউন্টে ট্রান্সফার লজিক
            }

            Spacer(modifier = Modifier.height(12.dp))

            TransferOptionItem(
                title = "Other Bank A/C Transfer",
                icon = Icons.Default.SyncAlt,
                brandColor = BrandGreen
            ) {
                // অন্য ব্যাংকে ট্রান্সফার লজিক
            }

            // ব্যাকগ্রাউন্ডের হালকা লোগো বা আর্টওয়ার্কের জন্য স্পেস
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun TransferOptionItem(
    title: String,
    icon: ImageVector,
    brandColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(1.dp, brandColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // বাম পাশের আইকন
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = brandColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // মাঝখানের টেক্সট
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = brandColor
        )

        // ডান পাশের অ্যারো
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = brandColor,
            modifier = Modifier.size(20.dp)
        )
    }
}