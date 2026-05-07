package com.wnapp.trustmoney.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.VerificationData
import com.wnapp.trustmoney.viewmodel.AppViewModel
import androidx.compose.foundation.combinedClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentUploadScreen(navController: NavController, viewModel: AppViewModel = viewModel()) {
    val context = LocalContext.current
    val userId = SessionManager(context).getUserId()

    val brandGreen = Color(0xFF1B5E20)
    val primaryText = Color(0xFF212121)
    val secondaryText = Color(0xFF757575)
    val screenBg = Color(0xFFFBFBFB)
    val cardBg = Color(0xFFFFFFFF)

    var docNumber by remember { mutableStateOf("") }
    var docType by remember { mutableStateOf("nid") }
    var frontUri by remember { mutableStateOf<Uri?>(null) }
    var backUri by remember { mutableStateOf<Uri?>(null) }

    val frontLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { frontUri = it }
    val backLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { backUri = it }

    LaunchedEffect(Unit) {
        viewModel.fetchVerificationStatus(userId)
        viewModel.refreshHistory(userId)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = screenBg) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Identity Verification", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = primaryText) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBackIosNew, "Back", tint = primaryText, modifier = Modifier.size(20.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = screenBg)
                )
            },
            containerColor = screenBg
        ) { padding ->
            // Use a Column for the whole screen to keep the list at the bottom
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Scrollable Content Area (Form or Status)
                Box(modifier = Modifier.weight(1f)) {
                    val statusData = viewModel.verificationData

                    if (statusData != null && statusData.status != "none") {
                        VerificationStatusContent(
                            status = statusData.status,
                            comment = statusData.adminComment,
                            brandGreen = brandGreen,
                            onReupload = { viewModel.verificationData = null }
                        )
                    } else {
                        // Your existing Form
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp)
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))

                            // Safety Banner
                            Box(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                                    .background(brandGreen.copy(alpha = 0.08f)).padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Security, null, tint = brandGreen, modifier = Modifier.size(32.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Secure Verification", color = brandGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("Your data is used for KYC only.", color = brandGreen.copy(0.8f), fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            SectionTitle("Select Document Type", brandGreen)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Tabs
                            Row(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEEEEEE)).padding(4.dp)
                            ) {
                                listOf("nid", "passport", "driving_licence").forEach { type ->
                                    val isSelected = docType == type
                                    Box(
                                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) brandGreen else Color.Transparent)
                                            .clickable { docType = type }.padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = type.replace("_", " ").uppercase(), color = if (isSelected) Color.White else secondaryText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            SectionTitle("Document Number", brandGreen)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = docNumber,
                                onValueChange = { docNumber = it },
                                placeholder = { Text("Enter your ID number", color = secondaryText) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = cardBg, unfocusedContainerColor = cardBg, focusedBorderColor = brandGreen, unfocusedBorderColor = Color(0xFFE0E0E0), focusedTextColor = primaryText, unfocusedTextColor = primaryText),
                                leadingIcon = { Icon(Icons.Default.Fingerprint, null, tint = brandGreen) }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                            SectionTitle("Upload Documents", brandGreen)
                            Spacer(modifier = Modifier.height(12.dp))

                            ProfessionalUploadBox(uri = frontUri, label = "Front Side of ${docType.uppercase()}", brandColor = brandGreen, textColor = primaryText) { frontLauncher.launch("image/*") }

                            if (docType != "passport") {
                                Spacer(modifier = Modifier.height(16.dp))
                                ProfessionalUploadBox(uri = backUri, label = "Back Side of ${docType.uppercase()}", brandColor = brandGreen, textColor = primaryText) { backLauncher.launch("image/*") }
                            }

                            Spacer(modifier = Modifier.height(30.dp))
                            Button(
                                onClick = { frontUri?.let { viewModel.submitVerification(userId, docType, docNumber, it, backUri, context) } },
                                enabled = frontUri != null && docNumber.isNotEmpty() && !viewModel.isLoading,
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = brandGreen)
                            ) {
                                if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                else Text("SUBMIT FOR REVIEW", fontWeight = FontWeight.ExtraBold)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                // --- PERSISTENT HISTORY LIST (Always at bottom) ---
                VerificationHistorySection(viewModel, userId,brandGreen)
            }
        }
    }
}


@Composable
fun VerificationHistorySection(viewModel: AppViewModel, userId: Int, brandGreen: Color) {
    val history = viewModel.verificationHistory
    val context = LocalContext.current

    // State to track which item is being deleted
    var itemToDelete by remember { mutableStateOf<VerificationData?>(null) }

    // Confirmation Dialog
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Record?") },
            text = { Text("Are you sure you want to permanently delete this verification request?") },
            confirmButton = {
                TextButton(onClick = {
                    itemToDelete?.id?.let { viewModel.deleteRecord(it, userId, context) }
                    itemToDelete = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xFFEEEEEE)))
            .padding(top = 12.dp)
            .heightIn(max = 250.dp)
    ) {
        Text(
            "Recent Requests (Long press to delete)",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Gray
        )

        if (history.isEmpty()) {
            Text("No history found", modifier = Modifier.fillMaxWidth().padding(24.dp), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(history) { item ->
                    // Pass the long press callback here
                    HistoryItem(
                        item = item,
                        brandGreen = brandGreen,
                        onLongClick = { itemToDelete = item }
                    )
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HistoryItem(item: VerificationData, brandGreen: Color, onLongClick: () -> Unit) {
    val statusColor = when (item.status) {
        "verified" -> Color(0xFF2E7D32)
        "rejected" -> Color(0xFFC62828)
        else -> Color(0xFFEF6C00)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F9FA))
            // Use combinedClickable for Long Click support
            .combinedClickable(
                onClick = { /* Optional: show details */ },
                onLongClick = onLongClick
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (item.status == "verified") Icons.Default.CheckCircle else Icons.Default.History,
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.docType?.uppercase() ?: "DOCUMENT", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("ID: ${item.docNumber ?: "N/A"}", fontSize = 11.sp, color = Color.Gray)
        }
        Text(item.status.uppercase(), color = statusColor, fontWeight = FontWeight.Black, fontSize = 10.sp)
    }
}
@Composable
fun VerificationStatusContent(status: String, comment: String?, brandGreen: Color, onReupload: () -> Unit) {
    val statusColor = when (status) {
        "verified" -> Color(0xFF2E7D32)
        "rejected" -> Color(0xFFC62828)
        else -> Color(0xFFEF6C00)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (status == "verified") Icons.Default.CheckCircle else if (status == "rejected") Icons.Default.Error else Icons.Default.PendingActions,
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(status.uppercase(), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = statusColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (status == "verified") "Identity Verified" else if (status == "rejected") "Verification Failed" else "Under Review",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        if (!comment.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(colors = CardDefaults.cardColors(containerColor = statusColor.copy(0.1f))) {
                Text("Note: $comment", modifier = Modifier.padding(16.dp), color = statusColor, fontSize = 14.sp)
            }
        }

        if (status == "rejected") {
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = onReupload, colors = ButtonDefaults.buttonColors(containerColor = brandGreen), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("RE-UPLOAD")
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, color: Color) {
    Text(text = text, fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
}

@Composable
fun ProfessionalUploadBox(uri: Uri?, label: String, brandColor: Color, textColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, if (uri != null) brandColor else Color(0xFFE0E0E0)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.TopEnd) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.background(Color.White, CircleShape))
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.FileUpload, null, tint = brandColor.copy(0.4f), modifier = Modifier.size(40.dp))
                Text(label, color = textColor.copy(0.6f), fontSize = 12.sp)
            }
        }
    }
}