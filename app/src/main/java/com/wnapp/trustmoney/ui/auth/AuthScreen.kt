package com.wnapp.trustmoney.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// লেটেস্ট লাইফসাইকেল এবং নেভিগেশন ইমপোর্ট
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// আপনার প্রজেক্টের নিজস্ব ক্লাসগুলো
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.local.getSavedLocale
import com.wnapp.trustmoney.data.local.updateLocale
import com.wnapp.trustmoney.data.repository.AuthRepository
import com.wnapp.trustmoney.data.utils.NotificationHelper
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.*
import com.wnapp.trustmoney.viewmodel.AuthViewModel
import com.wnapp.trustmoney.viewmodel.AuthViewModelFactory
import java.util.Locale

var generatedOtp by androidx.compose.runtime.mutableStateOf("")
var showOtpDialog by mutableStateOf(false)
var otpVerified by mutableStateOf(false)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(navController: NavController) {
    val context = LocalContext.current
    val currentLang = remember { getSavedLocale(context) } // বর্তমানে 'en' না কি 'bn' তা চেক করবে
    var selectedTab by remember { mutableIntStateOf(0) }
    val sm = SessionManager(context)
    val uriHandler = LocalUriHandler.current
    val repository = AuthRepository(context)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository)
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // সমস্ত কন্টেন্ট একটি Column এ রাখা হয়েছে যেন বাটন ওভারল্যাপ না হয়
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 25.dp).animateContentSize().verticalScroll(rememberScrollState()),   horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- ল্যাঙ্গুয়েজ সুইচ বাটন ---
            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp),contentAlignment = Alignment.TopEnd            ) {
                Row( modifier = Modifier.width(100.dp).height(38.dp).clip(RoundedCornerShape(19.dp)).background(Color.White.copy(alpha = 0.5f)).border(1.dp, TBL_Green_Dark.copy(alpha = 0.6f), RoundedCornerShape(19.dp)),verticalAlignment = Alignment.CenterVertically) {

                    // বাংলা বাটন
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().background(if (currentLang.startsWith("bn")) TBL_Green_Dark else Color.Transparent).clickable {
                                if (!currentLang.startsWith("bn")) {
                                    Toast.makeText(context, "বাংলা সেট হচ্ছে...", Toast.LENGTH_SHORT).show()
                                    updateLocale(context, "bn")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "বাং",color = if (currentLang.startsWith("bn")) Color.White else TBL_Green_Dark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                // English বাটন
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().background(if (currentLang.startsWith("en")) TBL_Green_Dark else Color.Transparent).clickable {
                                 // যদি ভাষা বাংলা থাকে (অর্থাৎ 'en' দিয়ে শুরু না হয়), তবেই ইংলিশ সেট হবে
                                if (!currentLang.startsWith("en")) {
                                    Toast.makeText(context, "Setting English...", Toast.LENGTH_SHORT).show()
                                    updateLocale(context, "en")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "EN",color = if (currentLang.startsWith("en")) Color.White else TBL_Green_Dark,fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            // 2. Logo
            Image(painter = painterResource(id = R.drawable.ic_trust_money_logo), contentDescription = null, modifier = Modifier.width(170.dp).height(85.dp))
            Spacer(modifier = Modifier.height(45.dp))

            // 3. Tab Switch (Login / New Request)
            Row(
                modifier = Modifier.fillMaxWidth().height(52.dp).border(1.dp, TBL_Green_Dark, RoundedCornerShape(26.dp)).padding(2.dp)
            ) {
                TabButton(stringResource(R.string.login_label), selectedTab == 0, Modifier.weight(1f)) {
                    selectedTab = 0
                }
                TabButton(stringResource(R.string.new_user_request), selectedTab == 1, Modifier.weight(1.4f)) {
                    selectedTab = 1
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // 4. Forms
            AnimatedContent(targetState = selectedTab, label = "TabTransition") { targetTab ->
                if (targetTab == 0) {
                    if(sm.isPinSet()){
                        PinLoginForm( onSuccess = {
                            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route)
                        })
                    }else{
                        LoginForm( authViewModel, context, { navController.navigate(Screen.Home.route) })
                    }
                   }else RegistrationForm( authViewModel ) {
                    Toast.makeText(context, "Registration processing...",Toast.LENGTH_SHORT).show()
                    triggerOtpProcess(context)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Bottom Services
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BottomServiceCard(Modifier.weight(1f).clickable { uriHandler.openUri("https://ekyc.tblbd.com") }, Icons.Outlined.PersonAdd, stringResource(R.string.open_account))
                BottomServiceCard(Modifier.weight(1f), Icons.Outlined.LocationOn, stringResource(R.string.nearby_atm))
                BottomServiceCard(Modifier.weight(1f), Icons.Outlined.BusinessCenter, stringResource(R.string.services_help))
            }

            Spacer(modifier = Modifier.height(60.dp))
            Text(text = "${stringResource(R.string.version)} 6.0", color = TBL_Green_Dark, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        }
    }
    if (showOtpDialog) {
        var otpInput by remember { androidx.compose.runtime.mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        AlertDialog(onDismissRequest = { }, title = { Text("Verify OTP", fontWeight = FontWeight.Bold) }, text = {
                Column {
                    Text("We've sent an OTP to your notification bar.")
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = {
                            if (it.length <= 6) otpInput = it
                            isError = false
                        },
                        label = { Text("Enter 6-digit OTP") },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (isError) {
                        Text("Invalid OTP, please try again", color = Color.Red, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (verifyOtp(otpInput)) {
                                navController.navigate(Screen.Home.route)
                        } else {
                            isError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark)
                ) {
                    Text("Verify")
                }
            }
        )
    }



}
@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) { Surface(modifier = modifier.fillMaxHeight().clickable { onClick() }, color = if (isSelected) TBL_Green_Dark else Color.Transparent, shape = RoundedCornerShape(26.dp)) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) Color.White else TBL_Green_Dark,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun BottomServiceCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Card(modifier = modifier.height(85.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, BrandGreen), shape = RoundedCornerShape(10.dp)
    ) {
        Row( modifier = Modifier.fillMaxSize().padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = title, fontSize = 11.sp, color = BrandGreen, fontWeight = FontWeight.Bold, lineHeight = 14.sp)
        }
    }
}



// ===========OTP Process ==================
fun triggerOtpProcess(context: Context){
    val random = java.security.SecureRandom()
    generatedOtp = (100000 + random.nextInt(900000)).toString()
    NotificationHelper(context).sendOtpNotification("Trust Money Security","Your Security OTP is: $generatedOtp. Do not share it.")
    showOtpDialog = true
}

fun verifyOtp(userInput: String): Boolean {
    return if (userInput == generatedOtp) {
        otpVerified = true
        showOtpDialog = false
        true
    } else {
        false
    }
}





@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    val navController = rememberNavController()
    AuthScreen(navController)
}