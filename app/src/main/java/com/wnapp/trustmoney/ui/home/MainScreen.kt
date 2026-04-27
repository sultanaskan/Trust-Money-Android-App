package com.wnapp.trustmoney.ui.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.BrandGreen
import com.wnapp.trustmoney.R

/**
 * সোর্স: Bottom Navigation & Scaffold
 * কনসেপ্ট: Single Activity - Multi Fragment/Screen
 * কাজ: নিচ থেকে ট্যাব পরিবর্তনের মাধ্যমে বিভিন্ন ফিচার এক্সেস করা।
 */
@Composable
fun MainScreen(mainNavController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            // BottomAppBar ব্যবহার করছি যাতে কাস্টম হাইট এবং FAB এর জন্য স্পেস দেওয়া যায়
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.height(70.dp), // বারটি ছোট রাখার জন্য হাইট ফিক্সড করা হয়েছে
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ১. Home
                    CustomNavItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = Icons.Default.Home,
                        label = "Home"
                    )

                    // ২. Gift (স্ক্রিনশট অনুযায়ী)
                    CustomNavItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = Icons.Default.CardGiftcard,
                        label = "Gift"
                    )

                    // ৩. মাঝখানের ফাঁকা জায়গা (FAB এর জন্য)
                    Spacer(modifier = Modifier.width(50.dp))

                    // ৪. History
                    CustomNavItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = Icons.Default.History,
                        label = "History"
                    )

                    // ৫. More
                    CustomNavItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = Icons.Default.GridView,
                        label = "More"
                    )
                }
            }
        },
        floatingActionButton = {
            // ৩. সবুজ রঙের Send FAB
            FloatingActionButton(
                onClick = { /* Send Money অ্যাকশন */ },
                containerColor = BrandGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(65.dp) // FAB এর সাইজ
                    .offset(y = 50.dp)
                    .rotate(-45f)
            ) {
                Icon(
                    imageVector = Icons.Default.Send, // সবুজ রঙের Send আইকন
                    contentDescription = "Send Money",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when(selectedTab) {
                0 -> HomeScreen()
                1 -> GiftScreen()
                2 -> HistoryScreen()
                3 -> MoreScreen(onMenuClick = { title ->
                    when (title) {
                        "Profile Information" -> mainNavController.navigate(Screen.Profile.route)
                        "Logout" -> {
                            // লগআউট লজিক: লগইন স্ক্রিনে পাঠিয়ে দেওয়া
                            mainNavController.navigate(Screen.Auth.route) {
                                popUpTo(0) // পুরো ব্যাকস্ট্যাক ক্লিয়ার করে দেওয়া (Concept: Security)
                            }
                        }
                        "Our Agent List" -> mainNavController.navigate(Screen.AgentList.route)
                        "Apply Business Loan" -> mainNavController.navigate(Screen.BusinessLoan.route)
                    }
            })

            }
        }
    }
}


@Composable
fun RowScope.CustomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) BrandGreen else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected) BrandGreen else Color.Gray,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}