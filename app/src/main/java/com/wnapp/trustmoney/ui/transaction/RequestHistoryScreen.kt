package com.wnapp.trustmoney.ui.transaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.MoneyRequest
import com.wnapp.trustmoney.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestHistoryScreen(
    navController: NavController,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    val sm = SessionManager(context)
    val userId = sm.getUserId()

    // Explicitly define light colors to ignore system dark mode
    val lightColors = lightColorScheme(
        primary = Color(0xFF0054A6),
        onPrimary = Color.White,
        surface = Color.White,
        onSurface = Color.Black,
        background = Color(0xFFF0F4F7),
        onBackground = Color.Black
    )

    LaunchedEffect(Unit) {
        viewModel.loadMoneyRequestHistory(userId)
    }

    // Force the MaterialTheme to use the lightColors scheme only
    MaterialTheme(colorScheme = lightColors) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Request History", fontWeight = FontWeight.Bold, color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(lightColors.background)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = lightColors.primary
                    )
                } else if (viewModel.requests.isEmpty()) {
                    Text(
                        "No requests found",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.requests) { request ->
                            HistoryItem(request)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(request: MoneyRequest) {
    val statusColor = when (request.status.lowercase()) {
        "approved" -> Color(0xFF00C853)
        "rejected" -> Color.Red
        else -> Color(0xFFFFA000) // Pending
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    request.type.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    request.paymentMethod,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                // Added safe substring check
                val dateText = if (request.createdAt.length >= 10) request.createdAt.substring(0, 10) else request.createdAt
                Text(dateText, fontSize = 11.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${request.amount} BDT",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF0054A6)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = request.status.replaceFirstChar { it.uppercase() },
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}