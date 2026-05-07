package com.wnapp.trustmoney.ui.transaction

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
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
import com.wnapp.trustmoney.data.model.TransactionModel
import com.wnapp.trustmoney.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(navController: NavController) {
    val context = LocalContext.current

    // ১. ফোর্সড লাইট মোড থিম সেটিংস
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF008346),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
            onSurface = Color.Black,
            onSurfaceVariant = Color.Gray
        )
    ) {
        val factory = remember { AppViewModel.AppViewModelFactory(context.applicationContext as Application) }
        val appVM: AppViewModel = viewModel(factory = factory)

        val sm = remember { SessionManager(context) }
        val userId = remember { sm.getUserId() }

        var selectedTransaction by remember { mutableStateOf<TransactionModel?>(null) }

        LaunchedEffect(userId) {
            if (userId != 0) {
                appVM.fetchTransactionHistory(userId)
            }
        }

        // ডিটেইল ডায়ালগ
        selectedTransaction?.let { txn ->
            TransactionDetailDialog(txn = txn, onDismiss = { selectedTransaction = null })
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Transaction History", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF008346))
                )
            }
        ) { padding ->
            // Surface ব্যবহার করা হয়েছে ব্যাকগ্রাউন্ড কালার নিশ্চিত করতে
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = Color(0xFFF5F5F5)
            ) {
                if (appVM.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF008346))
                    }
                } else if (appVM.transactionList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No Transactions Found", color = Color.Gray)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(appVM.transactionList) { txn ->
                            TransactionItem(txn = txn, onClick = { selectedTransaction = txn })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(txn: TransactionModel, onClick: () -> Unit) {
    val type = txn.type ?: "unknown"
    val icon = when (type) {
        "deposit" -> Icons.Default.AddCircle
        "withdraw" -> Icons.Default.RemoveCircle
        "payment" -> Icons.Default.ShoppingBag
        else -> Icons.Default.SwapHoriz
    }
    val color = if (type == "deposit") Color(0xFF008346) else Color.Red

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(color.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = txn.description ?: "Transaction",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Text(
                    text = txn.transactionId ?: "N/A",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (type == "deposit") "+" else "-"} ৳${txn.amount ?: "0"}",
                    color = color,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = (txn.status ?: "pending").replaceFirstChar { it.uppercase() },
                    fontSize = 11.sp,
                    color = if (txn.status == "success") Color(0xFF008346) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun TransactionDetailDialog(txn: TransactionModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color(0xFF008346), fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Text(text = "Transaction Details", fontWeight = FontWeight.Bold, color = Color.Black)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                DetailRow("ID", txn.transactionId ?: "N/A")
                DetailRow("Type", (txn.type ?: "N/A").replaceFirstChar { it.uppercase() })
                DetailRow("Amount", "৳${txn.amount ?: "0"}")
                DetailRow("Status", (txn.status ?: "Pending").replaceFirstChar { it.uppercase() })
                DetailRow("Description", txn.description ?: "N/A")
                DetailRow("Date", txn.createdAt ?: "N/A")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", fontWeight = FontWeight.SemiBold, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
    }
}