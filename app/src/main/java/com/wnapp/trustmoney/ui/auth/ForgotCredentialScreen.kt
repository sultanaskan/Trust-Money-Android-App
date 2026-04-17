package com.wnapp.trustmoney.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wnapp.trustmoney.data.model.RequestFormData
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark
import com.wnapp.trustmoney.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotCredentialsScreen(onBack: () -> Unit) {
    var resetType by remember { mutableStateOf("Account") } // Account or Card
    var formData by remember { mutableStateOf(RequestFormData()) } // আগের তৈরি করা মডেল ব্যবহার করতে পারেন
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ১. হেডার সেকশন (গাড় সবুজ ব্যাকগ্রাউন্ড)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TBL_Green_Dark)
                .padding(top = 40.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = White)
                }
                Text(
                    text = "Forgot ID & Password",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(Icons.Outlined.Home, contentDescription = null, tint = White)
            }
        }

        // ২. ফর্ম বডি
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Reset With", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

            Spacer(modifier = Modifier.height(10.dp))

            // কাস্টম সুইচ (Account | Card)
            CustomToggleSwitch(
                selectedOption = resetType,
                onOptionSelected = { resetType = it }
            )

            Spacer(modifier = Modifier.height(25.dp))

            // ৩. ইনপুট ফিল্ডস
            OutlinedTextField(
                value = formData.uniqueField,
                onValueChange = { formData = formData.copy(uniqueField = it) },
                placeholder = { Text("Enter your ${resetType.lowercase()} number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TBL_Green_Dark)
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = formData.phone,
                onValueChange = { formData = formData.copy(phone = it) },
                placeholder = { Text("Enter your registered number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TBL_Green_Dark)
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = formData.email,
                onValueChange = { formData = formData.copy(email = it) },
                placeholder = { Text("Enter your registered email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TBL_Green_Dark)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // ৪. সিকিউরিটি কোশ্চেন ড্রপডাউন (সংক্ষেপে)
            Box {
                OutlinedTextField(
                    value = formData.securityQuestion,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Security Question") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    shape = RoundedCornerShape(8.dp)
                )
                // ক্লিক করলে ড্রপডাউন ওপেন হওয়ার লজিক এখানে হবে (আগে যেমন দিয়েছি)
            }

            Text(
                text = "Forgot security question?",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                textAlign = TextAlign.End,
                color = TBL_Green_Dark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            OutlinedTextField(
                value = formData.securityAnswer,
                onValueChange = { formData = formData.copy(securityAnswer = it) },
                placeholder = { Text("Security answer") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

fun mutableStateOf(string: String) {}

@Composable
fun CustomToggleSwitch(selectedOption: String, onOptionSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(45.dp)
            .border(1.dp, TBL_Green_Dark, RoundedCornerShape(25.dp))

    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedOption == "Account") TBL_Green_Dark else Color.Transparent,
                    RoundedCornerShape(25.dp)
                )
                .clickable { onOptionSelected("Account") },
            contentAlignment = Alignment.Center
        ) {
            Text("Account", color = if (selectedOption == "Account") White else TBL_Green_Dark)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedOption == "Card") TBL_Green_Dark else Color.Transparent,
                    RoundedCornerShape(25.dp)
                )
                .clickable { onOptionSelected("Card") },
            contentAlignment = Alignment.Center
        ) {
            Text("Card", color = if (selectedOption == "Card") White else TBL_Green_Dark)
        }
    }
}

