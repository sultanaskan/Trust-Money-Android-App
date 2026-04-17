package com.wnapp.trustmoney.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wnapp.trustmoney.ui.theme.BrandGreen
import com.wnapp.trustmoney.ui.theme.White
import com.wnapp.trustmoney.R

/**
 * সোর্স: Scaffold & Scrollable Column
 * কনসেপ্ট: User Registration Flow
 * কাজ: নতুন ইউজারের তথ্য সংগ্রহ করা।
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen1(
    onBack: ()-> Unit,
    onContinue: ()-> Unit,
    onLoginBack: ()-> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(White).padding(24.dp)) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(30.dp).clickable { onBack() })

        Spacer(modifier = Modifier.height(40.dp))

        Text("Let's get started", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("Please enter your information", color = Color.Gray, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(40.dp))

        // কান্ট্রি সিলেক্টর
        OutlinedCard(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), modifier = Modifier.size(24.dp), contentDescription = null)
                Text("  UNITED KINGDOM", modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, null)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // নাম ইনপুট ফিল্ডস
        CustomInputField(placeholder = "First name or Given name", icon = Icons.Default.Person)
        Spacer(modifier = Modifier.height(20.dp))
        CustomInputField(placeholder = "Last Name or Surname", icon = Icons.Default.Person)
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onContinue() },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
        ) {
            Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Already have an account? ")
            Text("Log in", color = BrandGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onLoginBack() })
        }

        Spacer(modifier = Modifier.weight(1f))

        // সাপোর্ট বাটন
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
            SupportFab()
        }
    }
}


@Composable
fun CustomInputField(placeholder: String, icon: ImageVector) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color.Gray) },
        leadingIcon = { Icon(icon, null, tint = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun SupportFab(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(BrandGreen, RoundedCornerShape(50.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Chat, contentDescription = null, tint = White)
            Text("  24/7 Support", color = White, fontWeight = FontWeight.Bold)
        }
        // লাল ক্লোজ বাটন
        Surface(
            modifier = Modifier.size(24.dp).offset(x = 100.dp, y = (-10).dp), // এডজাস্ট করে নিবেন
            shape = CircleShape,
            color = Color.Red
        ) {
            Icon(Icons.Default.Close, null, tint = White, modifier = Modifier.padding(4.dp))
        }
    }
}