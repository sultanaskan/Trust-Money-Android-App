package com.wnapp.trustmoney.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.R

@Composable
fun InsufficientBalanceScreen(navController: NavController) {
    // রিসোর্স থেকে স্ট্রিংগুলো গেট করা
    val title = stringResource(id = R.string.insufficient_balance_title)
    val description = stringResource(id = R.string.insufficient_balance_desc)
    val step1 = stringResource(id = R.string.step_1)
    val step2 = stringResource(id = R.string.step_2)
    val step3 = stringResource(id = R.string.step_3)
    val btnText = stringResource(id = R.string.add_money_continue)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ১. সতর্কবার্তা আইকন
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = "No Balance",
            tint = Color(0xFFE57373),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ২. মূল শিরোনাম
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ৩. ইউজারকে গাইড করা
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ৪. ডিপোজিট গাইডলাইন লিস্ট
        GuideStepItem(step = step1)
        GuideStepItem(step = step2)
        GuideStepItem(step = step3)

        Spacer(modifier = Modifier.height(40.dp))

        // ৫. কন্টিনিউ বাটন
        Button(
            onClick = {
                navController.navigate(Screen.AddBalance.passAmount("0"))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008346)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = btnText, color = Color.White, fontSize = 16.sp)
        }
    }
}
@Composable
fun GuideStepItem(step: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF008346),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = step, fontSize = 13.sp, color = Color.DarkGray)
    }
}