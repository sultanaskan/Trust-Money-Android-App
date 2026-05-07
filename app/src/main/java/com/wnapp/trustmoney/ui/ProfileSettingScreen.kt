package com.wnapp.trustmoney.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.local.saveImageToInternalStorage
import com.wnapp.trustmoney.ui.navigation.Screen
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingScreen(navController: NavController) {
    val context = LocalContext.current
    val sm = remember { SessionManager(context) }

    // Colors
    val trustGreen = Color(0xFF008346)
    val backgroundGray = Color(0xFFF3F4F6)
    val textDark = Color(0xFF111827)

    // Data
    val userName = remember { sm.getFullName() ?: "User Name" }
    val userPhone = remember { sm.getUserPhone() ?: "Not Set" }
    val userEmail = remember { sm.getEmail() ?: "Not Set" }

    // ইমেজ লোড করার লজিক: প্রথমে দেখা হয় ইন্টারনাল স্টোরেজে ফাইল আছে কিনা
    var profilePictureUri by remember {
        mutableStateOf<Uri?>(
            File(context.filesDir, "profile_picture.jpg").let { file ->
                Uri.fromFile(file)
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri ->
            val internalUri = saveImageToInternalStorage(context, selectedUri)
            if (internalUri != null) {
                profilePictureUri = internalUri
                sm.saveProfileImageUri(internalUri.toString())
            }
        }
    }

    Scaffold(containerColor = backgroundGray) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ১. হেডার সেকশন
            Box(
                modifier = Modifier.fillMaxWidth().height(260.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                        .background(trustGreen, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(top = 40.dp, start = 10.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                        Text("My Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                // প্রোফাইল পিকচার Display
                Box(modifier = Modifier.align(Alignment.BottomCenter), contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        modifier = Modifier.size(140.dp).border(6.dp, Color.White, CircleShape),
                        shape = CircleShape,
                        shadowElevation = 12.dp
                    ) {
                        if (profilePictureUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profilePictureUri),
                                contentDescription = null,
                                modifier = Modifier.clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.background(Color.White), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(70.dp), tint = trustGreen.copy(alpha = 0.5f))
                            }
                        }
                    }

                    SmallFloatingActionButton(
                        onClick = { launcher.launch("image/*") },
                        shape = CircleShape,
                        containerColor = Color.White,
                        contentColor = trustGreen,
                        modifier = Modifier.size(42.dp).offset(x = (-4).dp, y = (-4).dp).border(1.dp, trustGreen.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = userName, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 24.sp, fontWeight = FontWeight.Black, color = textDark)
            Text(text = "Verified Account", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 14.sp, color = trustGreen, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(32.dp))

            // ২. ইনফরমেশন কার্ড
            Card(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    PremiumInfoItem(Icons.Default.PersonOutline, "Full Name", userName, trustGreen)
                    HorizontalDivider(Modifier.padding(vertical = 16.dp), color = backgroundGray)
                    PremiumInfoItem(Icons.Default.PhoneAndroid, "Phone Number", userPhone, trustGreen)
                    HorizontalDivider(Modifier.padding(vertical = 16.dp), color = backgroundGray)
                    PremiumInfoItem(Icons.Default.MailOutline, "Email Address", userEmail, trustGreen)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ৩. বাটন সেকশন
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        SessionManager(context).clearSession()
                        navController.navigate(Screen.Auth.route)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color.Red.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Logout from Account", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun PremiumInfoItem(icon: ImageVector, label: String, value: String, iconColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconColor.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(value, color = Color(0xFF1F2937), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}