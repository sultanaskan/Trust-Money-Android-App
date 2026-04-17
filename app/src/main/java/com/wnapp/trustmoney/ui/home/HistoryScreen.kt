package com.wnapp.trustmoney.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wnapp.trustmoney.ui.theme.BrandGreen
import com.wnapp.trustmoney.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // ১. হেডার সেকশন (Transactions & Notification)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Transactions", //
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .background(BrandGreen, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ২. সার্চ বার এবং ফিল্টার আইকন
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            placeholder = {
                Text(
                    "Transaction ID, phone number, bill number", //
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.FilterList, // ফিল্টার আইকন
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(50.dp), // পিল শেপড সার্চ বার
            colors = TextFieldDefaults.colors(
                // OutlinedTextField এর জন্য নিচের প্রপার্টিগুলো ব্যবহার করুন
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.LightGray, // এটিই বর্ডার কালার
                focusedIndicatorColor = BrandGreen,        // এটিই বর্ডার কালsার
                cursorColor = BrandGreen
            ),
            singleLine = true
        )

        // ৩. ট্রানজ্যাকশন লিস্ট (বর্তমানে খালি রাখা হয়েছে স্ক্রিনশট অনুযায়ী)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // যদি কোনো ডাটা না থাকে তবে এখানে Empty State দেখানো যেতে পারে
        }
    }
}