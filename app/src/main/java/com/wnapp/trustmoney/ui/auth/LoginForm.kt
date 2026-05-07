package com.wnapp.trustmoney.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.repository.AuthRepository
import com.wnapp.trustmoney.data.utils.NotificationHelper
import com.wnapp.trustmoney.ui.components.OtpVerificationDialog
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark
import com.wnapp.trustmoney.viewmodel.AuthViewModel


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginForm( viewModel: AuthViewModel, context: Context, onSuccess: () -> Unit) {
    var creds by remember { mutableStateOf(LoginCreds()) }
    var isPasswordVisible by remember { mutableStateOf(false) }


    val loginStatus by viewModel.loginStatus
    val isLoading by viewModel.isLoading

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        // Email Field
        OutlinedTextField(
            value = creds.email,
            onValueChange = { creds = creds.copy(email = it) },
            label = { Text(stringResource(R.string.email_address_lable), color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Person, null, tint = TBL_Green_Dark) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = Color.LightGray, // যখন সিলেক্ট থাকবে না তখন বর্ডার কালার
                focusedTextColor = TBL_Green_Dark,        // যখন টাইপ করবেন তখন টেক্সট কালার
                unfocusedTextColor = Color.Black,      // টাইপ করার পর সিলেক্ট না থাকলেও টেক্সট কালার
                cursorColor = TBL_Green_Dark           // কার্সার এর কালার
            ),
        )

        // Password Field
        OutlinedTextField(
            value = creds.password,
            onValueChange = { creds = creds.copy(password = it) },
            label = { Text(stringResource(R.string.password_lable), color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = TBL_Green_Dark) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = TBL_Green_Dark
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = Color.LightGray, // যখন সিলেক্ট থাকবে না তখন বর্ডার কালার
                focusedTextColor = TBL_Green_Dark,        // যখন টাইপ করবেন তখন টেক্সট কালার
                unfocusedTextColor = Color.Black,      // টাইপ করার পর সিলেক্ট না থাকলেও টেক্সট কালার
                cursorColor = TBL_Green_Dark           // কার্সার এর কালার
            ),
            singleLine = true
        )

        // এরর মেসেজ
        if (loginStatus != null && loginStatus != "Success") {
            Text(text = loginStatus!!, color = Color.Red, fontSize = 12.sp)
        }

        // Login Button
        Button(
            onClick = {
                viewModel.loginUser(creds, {
                    onSuccess()
                })
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    stringResource(R.string.login_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}








