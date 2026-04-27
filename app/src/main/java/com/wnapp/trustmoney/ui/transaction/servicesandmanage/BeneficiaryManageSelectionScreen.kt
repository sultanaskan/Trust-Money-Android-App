package com.wnapp.trustmoney.ui.transaction.servicesandmanage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryManageSelectionScreen(navController: NavController) {
    val brandGreen = Color(0xFF004D40) // স্ক্রিনশটের গাঢ় সবুজ থিম

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Beneficiary Manage",
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
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
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
                    containerColor = brandGreen
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
            // ১. ইনস্ট্রাকশন কার্ড (You need to select the Beneficiary Manage)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FBF8))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("You need to select the ")
                        withStyle(style = SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                            append("Beneficiary Manage")
                        }
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ২. বেনিফিশিয়ারি অপশন লিস্ট
            BeneficiaryOptionItem("Trust Bank", brandGreen) {
                // navController.navigate("trust_bank_beneficiary")
            }

            Spacer(modifier = Modifier.height(12.dp))

            BeneficiaryOptionItem("Other Bank", brandGreen) {
                // navController.navigate("other_bank_beneficiary")
            }

            Spacer(modifier = Modifier.height(12.dp))

            BeneficiaryOptionItem("Mobile Financial Service (MFS)", brandGreen) {
                // navController.navigate("mfs_beneficiary")
            }

            // ব্যাকগ্রাউন্ডের হালকা ওয়াটারমার্ক বা আর্টওয়ার্কের জন্য স্পেস
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun BeneficiaryOptionItem(
    title: String,
    brandColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, brandColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // অপশন টাইটেল
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