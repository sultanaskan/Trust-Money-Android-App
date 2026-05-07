package com.wnapp.trustmoney.ui.transaction.methodselection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileRechargeMethodSelectionScreen(navController: NavController) {
    val brandGreen = Color(0xFF004D40) // স্ক্রিনশটের গাঢ় সবুজ
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mobile Recharge", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) { popUpTo("home") { inclusive = true } } }) {
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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // ১. ব্যালেন্স সেকশন
            Text(
                text = "BDT 0.00",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                color = Color(0xFF00796B),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // ২. "Fill in the Required Fields" হেডার
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EditNote, contentDescription = null, tint = brandGreen, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fill in the Required Fields", color = Color.Gray, fontSize = 14.sp)
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // ৩. ইনপুট ফিল্ডস (Dropdowns and TextFields)
            RechargeDropdownField("From Account / Card")
            RechargeTextField("Mobile No (Eg. 01XXXXXXXXX)", trailingIcon = Icons.Default.ContactPage)
            RechargeDropdownField("Select Operator Name")
            RechargeDropdownField("Connection Type")
            RechargeTextField("Amount")
            RechargeTextField("Remarks")

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // ৪. নেক্সট বাটন
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandGreen)
            ) {
                Text("NEXT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun RechargeTextField(label: String, trailingIcon: ImageVector? = null) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label, fontSize = 14.sp, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        trailingIcon = {
            if (trailingIcon != null) {
                Icon(trailingIcon, contentDescription = null, tint = Color(0xFF004D40))
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF004D40),
            unfocusedBorderColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

@Composable
fun RechargeDropdownField(label: String) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(label, fontSize = 14.sp, color = Color.Gray) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF004D40),
            unfocusedBorderColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp)
    )
}