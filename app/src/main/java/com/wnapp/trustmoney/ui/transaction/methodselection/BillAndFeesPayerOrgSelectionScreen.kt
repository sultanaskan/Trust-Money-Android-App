package com.wnapp.trustmoney.ui.transaction.methodselection


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillAndFeesPayerOrgSelectionScreen(navController: NavController) {
    val brandGreen = Color(0xFF004D40)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bills & Fees Payment", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } } }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = brandGreen)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ১. "Select your preferred Bill Type" হেডার
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FBF8), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Select your preferred ")
                        withStyle(style = SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                            append("Bill Type")
                        }
                    },
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ২. সার্চ বার
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter organization's name", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF00796B)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ৩. ক্যাটাগরি গ্রিড (Gas, Water, Electricity...)
            Card(
                modifier = Modifier.fillMaxWidth().border(0.5.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val categories = listOf(
                        Triple("Gas", Icons.Default.LocalFireDepartment, Color(0xFFFF7043)),
                        Triple("Water", Icons.Default.WaterDrop, Color(0xFF42A5F5)),
                        Triple("Electricity", Icons.Default.Lightbulb, Color(0xFFFFCA28)),
                        Triple("Govt Payment", Icons.Default.AccountBalance, Color.Gray),
                        Triple("Insurance", Icons.Default.Shield, Color.Gray),
                        Triple("Tuition Fees", Icons.Default.School, Color.Gray)
                    )

                    // ২ সারিতে ৩টি করে আইটেম
                    categories.chunked(3).forEach { rowItems ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            rowItems.forEach { item ->
                                BillCategoryItem(item.first, item.second, item.third)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ৪. অর্গানাইজেশন লিস্ট
            val organizations = listOf("DESCO (Postpaid)", "Jalalabad Gas", "DESCO (PrePaid)", "NESCO Prepaid", "A-Challan")

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(organizations) { org ->
                    BillOrgListItem(org, brandGreen)
                }
            }
        }
    }
}

@Composable
fun BillCategoryItem(title: String, icon: ImageVector, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
    }
}

@Composable
fun BillOrgListItem(name: String, brandColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .border(1.dp, brandColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clickable { /* Action */ }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Adjust, contentDescription = null, tint = brandColor, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = name, modifier = Modifier.weight(1f), fontSize = 15.sp, color = brandColor, fontWeight = FontWeight.SemiBold)
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = brandColor, modifier = Modifier.size(18.dp))
    }
}