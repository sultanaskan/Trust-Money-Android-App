package com.wnapp.trustmoney.ui.auth

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.repository.AuthRepository
import com.wnapp.trustmoney.ui.theme.*
import com.wnapp.trustmoney.ui.viewmodel.AuthViewModel
import com.wnapp.trustmoney.ui.viewmodel.AuthViewModelFactory

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val uriHandler = LocalUriHandler.current
    val repository = AuthRepository()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository) // ফ্যাক্টরি ব্যবহার করা জরুরি
    )


    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)
                // Use animateContentSize so the scroll container handles height changes smoothly
                .animateContentSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            // 2. Logo
            Image(
                painter = painterResource(id = R.drawable.ic_trust_money_logo),
                contentDescription = null,
                modifier = Modifier.width(170.dp).height(85.dp)
            )
            Spacer(modifier = Modifier.height(45.dp))
            // 3. Tab Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, TBL_Green_Dark, RoundedCornerShape(26.dp))
                    .padding(2.dp)
            ) {
                TabButton("Login", selectedTab == 0, Modifier.weight(1f)) {
                    selectedTab = 0
                }
                TabButton("New user Request", selectedTab == 1, Modifier.weight(1.4f)) {
                    selectedTab = 1
                }
            }
            Spacer(modifier = Modifier.height(35.dp))

            // 4. Content Switching (Fixed)
            androidx.compose.animation.AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "TabTransition"
            ) { targetTab ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (targetTab == 0) {
                        // LoginForm-এ viewModel প্যারামিটারটি যুক্ত করা হয়েছে
                        LoginForm(
                            navController = navController,
                            viewModel = authViewModel
                        )
                    } else {
                        RegistrationForm(
                            navController = navController,
                            viewModel = authViewModel,
                            onRegistrationSuccess = {
                                selectedTab = 0 // রেজিস্ট্রেশন সফল হলে লগইন ট্যাবে নিয়ে যাবে
                            }
                        )
                    }
                }
            }


            // ... (Rest of your code for BottomServiceCards)
            Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BottomServiceCard(Modifier.weight(1f).clickable{uriHandler.openUri("https://ekyc.tblbd.com")}, Icons.Outlined.PersonAdd, "Open an\naccount")
                BottomServiceCard(Modifier.weight(1f), Icons.Outlined.LocationOn, "Nearby\nBR/ATM")
                BottomServiceCard(Modifier.weight(1f), Icons.Outlined.BusinessCenter, "Services\n& Help")
            }
            Spacer(modifier = Modifier.height(60.dp))
            Text("Version 6.0", color = TBL_Green_Dark, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        }
    }
}






@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.fillMaxHeight().clickable { onClick() },
        color = if (isSelected) TBL_Green_Dark else Color.Transparent,
        shape = RoundedCornerShape(26.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) White else TBL_Green_Dark,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}


@Composable
fun BottomServiceCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Card(
        modifier = modifier.height(85.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, BrandGreen),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = BrandGreen,
                fontWeight = FontWeight.Bold,
                lineHeight = 14.sp
            )
        }
    }
}




@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    NECMoneyTheme {
        LoginForm(navController = rememberNavController(), viewModel = AuthViewModel(AuthRepository()))
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationFormPreview() {
    // ১. একটি ডামি রিপোজিটরি (যদি সম্ভব হয়)
    val repository = AuthRepository()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository) // ফ্যাক্টরি ব্যবহার করা জরুরি
    )
    NECMoneyTheme {
        RegistrationForm(
            navController = rememberNavController(),
            viewModel = authViewModel,
            onRegistrationSuccess = {}
        )
    }
}