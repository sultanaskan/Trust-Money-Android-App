package com.wnapp.trustmoney.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wnapp.trustmoney.ui.theme.BrandGreen

@Composable
fun MoreScreen(onMenuClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ১. হেডার (More Title & Notification)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "More",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { },
                modifier = Modifier.background(BrandGreen, CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ২. প্রোফাইল সেকশন ও ব্যালেন্স
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(60.dp).background(BrandGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(35.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Hello,", color = BrandGreen, fontSize = 14.sp)
                    Text("Md Rasel Mollah", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // ব্যালেন্স চিপ
            Surface(
                color = BrandGreen,
                shape = RoundedCornerShape(50.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("BDT 0.00", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Wallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ৩. মেনু গ্রিড সেকশন (Card এর ভেতরে)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),

        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val menuItems = listOf(
                    MenuItemData("Profile Information", Icons.Default.AccountCircle),
                    MenuItemData("Upload Document", Icons.Default.UploadFile),
                    MenuItemData("View Document", Icons.Default.Description),
                    MenuItemData("Tracking Transaction", Icons.Default.LocationOn),
                    MenuItemData("Apply Business Loan", Icons.Default.Assignment),
                    MenuItemData("Our Agent List", Icons.Default.ShoppingCart),
                    MenuItemData("View My Offer", Icons.Default.CardGiftcard),
                    MenuItemData("Helpline 24/7", Icons.Default.SupportAgent),
                    MenuItemData("My Account Logout", Icons.Default.Logout, isLogout = true)
                )

                // ৩টি করে কলামে মেনু আইটেম সাজানো
                menuItems.chunked(3).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        rowItems.forEach { item ->
                            MoreMenuItem(
                                item = item,
                                onClick = {item.title}
                            )
                        }
                        // যদি রো-তে ৩টির কম আইটেম থাকে তবে খালি জায়গা রাখা
                        if (rowItems.size < 3) {
                            repeat(3 - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun RowScope.MoreMenuItem(item: MenuItemData, onClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(70.dp).clickable{ onClick()} ,
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (item.isLogout) Color.Red else BrandGreen,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}

data class MenuItemData(
    val title: String,
    val icon: ImageVector,
    val isLogout: Boolean = false
)