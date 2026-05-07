package com.wnapp.trustmoney.ui.transaction

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.TransactionType
import com.wnapp.trustmoney.data.utils.NotificationHelper
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.transaction.paymentsubmitcomponents.AmountDisplayBox
import com.wnapp.trustmoney.viewmodel.AppViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositWithBankScreen(
    navController: NavController,
    amount: String,
    paymentMethodId: String,
    viewModel: AppViewModel = viewModel()
) {
    // এখানে Force Light Mode থিম অ্যাপ্লাই করা হয়েছে
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF0054A6),
            background = Color(0xFFF0F4F7),
            surface = Color.White,
            onSurface = Color.Black
        )
    ) {
        val context = LocalContext.current
        val bankBlue = Color(0xFF0054A6)
        val successGreen = Color(0xFF00C853)
        val mc = MyCurrency(context)
        val sm = SessionManager(context)

        val userId = remember { sm.getUserId() }
        val currencyName = mc.getCurrencyName()
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        val paymentMethod = viewModel.paymentMethods.value.find { it.id == paymentMethodId.toInt() }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
        }

        // ক্লিপবোর্ড ফাংশন
        fun copyToClipboard(text: String, label: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "$label কপি হয়েছে", Toast.LENGTH_SHORT).show()
        }

        // --- সাকসেস কনফার্মেশন ডায়ালগ ---
        if (viewModel.isRequestSuccessful) {
            AlertDialog(
                onDismissRequest = { viewModel.isRequestSuccessful = false },
                title = { Text("সফল হয়েছে", fontWeight = FontWeight.Bold, color = Color.Black) },
                text = { Text("আপনার ব্যাংক ডিপোজিট রিকোয়েস্টটি সফলভাবে পাঠানো হয়েছে। অনুগ্রহ করে আমাদের ভেরিফিকেশনের জন্য অপেক্ষা করুন।", color = Color.DarkGray) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.isRequestSuccessful = false

                        NotificationHelper(context).sendOtpNotification(
                            title = "Add Money request sent!",
                            content = "We received your add money request.\nAmount: $amount $currencyName\nOur team will verify it soon."
                        )

                        navController.navigate(Screen.Home.route)
                    }) {
                        Text("ঠিক আছে", color = successGreen, fontWeight = FontWeight.Bold)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color.White
            )
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Bank Payment", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.KeyboardArrowLeft, "Back", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF0F4F7))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = paymentMethod?.providerName ?: "Bank Transfer",
                        color = bankBlue,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    AmountDisplayBox(amount, currencyName)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(successGreen, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .padding(24.dp)
                ) {
                    Text("Payment Document Upload", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    // ব্যাংক ডিটেইলস কার্ড
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Bank Details", fontWeight = FontWeight.Bold, color = bankBlue)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)

                            // Account Holder Row
                            val holderName = "ISTEYAK AHMAD"
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Account Holder: $holderName", fontSize = 14.sp, color = Color.Black, modifier = Modifier.weight(1f))
                                IconButton(onClick = { copyToClipboard(holderName, "নাম") }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = bankBlue, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Account Number Row
                            val accNo = "539000010006"
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Account Number: $accNo", fontSize = 14.sp, color = Color.Black, modifier = Modifier.weight(1f))
                                IconButton(onClick = { copyToClipboard(accNo, "অ্যাকাউন্ট নাম্বার") }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = bankBlue, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Bank: ${paymentMethod?.providerName ?: "Selected Bank"}", fontSize = 14.sp, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Receipt",
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = successGreen, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("ট্রান্সফার স্লিপ আপলোড করুন", color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            selectedImageUri?.let { uri ->
                                val file = FileUtil.getFileFromUri(context, uri)
                                viewModel.submitBankRequest(
                                    userId = userId,
                                    method = paymentMethod?.providerName ?: "Bank",
                                    amount = amount,
                                    type = TransactionType.deposit,
                                    imageFile = file,
                                    context = context
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selectedImageUri != null && !viewModel.isLoading
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = successGreen, strokeWidth = 2.dp)
                        } else {
                            Text("জমা দিন", color = successGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

object FileUtil {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_receipt_${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            if (file.exists()) file else null
        } catch (e: Exception) {
            null
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DepositWithBankScreenPreview() {
    val navController = rememberNavController()
    DepositWithBankScreen(navController = navController, amount = "500", paymentMethodId = "1")
}