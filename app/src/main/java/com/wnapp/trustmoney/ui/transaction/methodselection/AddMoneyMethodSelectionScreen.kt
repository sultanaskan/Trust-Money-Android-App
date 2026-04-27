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
fun AddMoneyMethodSelectionScreen(navController: NavController) {
    val brandGreen = Color(0xFF004D40) // স্ক্রিনশটের গাঢ় সবুজ থিম

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add Money",
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
                        navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = true }
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
            // ১. "Select your preferred Wallet Type" বক্স
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FBF8), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Select your preferred ")
                        withStyle(style = SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                            append("Wallet Type")
                        }
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ২. ওয়ালেট অপশন লিস্ট
            // এখানে R.drawable.tap, R.drawable.bkash ইত্যাদি ইমেজ থাকতে হবে
            WalletOptionItem("TAP", "tap_logo", brandGreen) { /* Action */ }
            Spacer(modifier = Modifier.height(12.dp))

            WalletOptionItem("bKash", "bkash_logo", brandGreen) { /* Action */ }
            Spacer(modifier = Modifier.height(12.dp))

            WalletOptionItem("Nagad", "nagad_logo", brandGreen) { /* Action */ }
            Spacer(modifier = Modifier.height(12.dp))

            WalletOptionItem("Sheba Pay", "sheba_pay_logo", brandGreen) { /* Action */ }

            // নিচের হালকা ওয়াটারমার্ক বা আর্টওয়ার্কের জন্য স্পেস
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun WalletOptionItem(
    title: String,
    logoResName: String, // আপনার লোগোর নাম
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
        // লোগো (এখানে ডামি বক্স রাখা হয়েছে, আপনি Image ব্যবহার করবেন)
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // উদাহরণ: Image(painter = painterResource(id = R.drawable.bkash), contentDescription = null)
            Text(title.take(1), fontWeight = FontWeight.Bold, color = brandColor)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // নাম
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = brandColor
        )

        // অ্যারো
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = brandColor,
            modifier = Modifier.size(22.dp)
        )
    }
}