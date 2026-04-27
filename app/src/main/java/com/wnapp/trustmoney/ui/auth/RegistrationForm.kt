package com.wnapp.trustmoney.ui.auth

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.TBL_Divider_Gray
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark
import com.wnapp.trustmoney.ui.viewmodel.AuthViewModel
import com.wnapp.trustmoney.util.getSvgImageLoader
import java.util.Calendar


@Composable
fun RegistrationForm(
    navController: NavController,
    viewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit
) {
    // ফর্ম ডাটা স্টেট
    var formData by remember { mutableStateOf(RegistrationFormData()) }

    // ViewModel থেকে স্টেটগুলো নেওয়া
    val countries by viewModel.countries
    val isLoading by viewModel.isLoading
    val registrationStatus by viewModel.registrationStatus
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

// ক্যালেন্ডার ডায়ালগ সেটআপ
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // মাস ০ থেকে শুরু হয় তাই ১ যোগ করতে হয়
            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            formData = formData.copy(dateOfBirth = formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // রেজিস্ট্রেশন সফল হলে নেভিগেশন লজিক
    LaunchedEffect(registrationStatus) {
        if (registrationStatus == "Success") {
            onRegistrationSuccess()
            viewModel.resetRegistrationStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            color = TBL_Green_Dark,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ১. Country Dropdown (ID Handling)
        SelectableServerField(
            selectedId = formData.currencyId, // formData থেকে সরাসরি ID দিন
            onValueChange = { newId ->
                formData = formData.copy(currencyId = newId) // সরাসরি ID আপডেট করুন
            },
            label = "Country",
            defaultIcon = Icons.Outlined.Flag,
            suggestions = countries,
            isLoading = isLoading,
            onFocus = { if (countries.isEmpty()) viewModel.getCurrency() },
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )
        // ২. First Name
        CustomTextField(
            value = formData.firstName,
            onValueChange = { formData = formData.copy(firstName = it) },
            label = "First Name",
            icon = Icons.Outlined.Person,
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )

        // ৩. Last Name
        CustomTextField(
            value = formData.lastName,
            onValueChange = { formData = formData.copy(lastName = it) },
            label = "Last Name",
            icon = Icons.Outlined.Person,
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )

        // ৪. Phone Number
        CustomTextField(
            value = formData.phone,
            onValueChange = { formData = formData.copy(phone = it) },
            label = "Phone Number",
            icon = Icons.Outlined.Phone,
            keyboardType = KeyboardType.Phone,
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )

        // ৫. Email Address
        CustomTextField(
            value = formData.email,
            onValueChange = { formData = formData.copy(email = it) },
            label = "Email Address",
            icon = Icons.Outlined.Email,
            keyboardType = KeyboardType.Email,
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )

        // ৬. Date of Birth
        // ৬. Date of Birth
        Box(modifier = Modifier.fillMaxWidth()) {
            CustomTextField(
                value = formData.dateOfBirth,
                onValueChange = { }, // সরাসরি টাইপ করা বন্ধ রাখতে খালি রাখুন
                label = "Date of Birth",
                icon = Icons.Outlined.CalendarMonth,
                readOnly = true, // ইউজার যাতে কিবোর্ড দিয়ে লিখতে না পারে
                colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() } // বক্সে ক্লিক করলে ক্যালেন্ডার খুলবে
            )

            // একটি স্বচ্ছ লেয়ার যা ক্লিক ইভেন্টটি ধরবে
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { datePickerDialog.show() }
            )
        }

        // ৭. Password
        CustomTextField(
            value = formData.password,
            onValueChange = { formData = formData.copy(password = it) },
            label = "Password",
            icon = Icons.Outlined.Lock,
            isPassword = true,
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )

        // ৮. Confirm Password
        CustomTextField(
            value = formData.confirmPassword,
            onValueChange = { formData = formData.copy(confirmPassword = it) },
            label = "Confirm Password",
            icon = Icons.Outlined.Lock,
            isPassword = true,
            colors = textFieldColors(TBL_Green_Dark, TBL_Divider_Gray)
        )

        // এরর মেসেজ ডিসপ্লে (যদি থাকে)
        if (registrationStatus != null && registrationStatus != "Success") {
            Text(
                text = registrationStatus!!,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // ৯. Submit Button
        Button(
            onClick = {
                if (!isLoading) {
                    viewModel.registerUser(formData)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(top = 16.dp),
            enabled = !isLoading, // লোড হওয়ার সময় বাটন ক্লিক হবে না
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Register", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableServerField(
    selectedId: Int, // value এর বদলে selectedId (Int) ব্যবহার করা হয়েছে
    onValueChange: (Int) -> Unit, // এখানে এখন সরাসরি Int (ID) পাঠানো হবে
    label: String,
    defaultIcon: ImageVector,
    suggestions: List<CurrencyItem>,
    isLoading: Boolean = false,
    onFocus: () -> Unit,
    colors: TextFieldColors
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imageLoader = remember { getSvgImageLoader(context) }

    // বর্তমানে সিলেক্ট করা দেশের অবজেক্ট খুঁজে বের করা
    val selectedCountry = suggestions.find { it.id == selectedId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            // টেক্সটবক্সে দেখানোর জন্য দেশের নাম ব্যবহার করা হয়েছে, ID নয়
            value = selectedCountry?.countryName ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .onFocusChanged { if (it.isFocused) onFocus() },
            leadingIcon = {
                if (selectedCountry != null) {
                    AsyncImage(
                        model = selectedCountry.flagUrl.replace("http://", "https://"),
                        imageLoader = imageLoader,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp, 18.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Icon(imageVector = defaultIcon, contentDescription = null, tint = Color(0xFF1B5E20))
                }
            },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF1B5E20)
                    )
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = colors,
            singleLine = true
        )

        // ড্রপডাউন মেনু
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White)
        ) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                suggestions.forEachIndexed { index, country ->
                    Column {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = country.flagUrl.replace("http://", "https://"),
                                        imageLoader = imageLoader,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(30.dp, 20.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        contentScale = ContentScale.FillBounds
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = country.countryName,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            },
                            onClick = {
                                onValueChange(country.id) // সরাসরি দেশের ID পাঠানো হচ্ছে
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )

                        if (index < suggestions.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    readOnly: Boolean = false, // নতুন যোগ করা হয়েছে (ডেট পিকারের জন্য প্রয়োজন)
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier.fillMaxWidth(), // মডিফায়ার পাস করার সুবিধা
    colors: TextFieldColors
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        readOnly = readOnly, // কিবোর্ড টাইপিং কন্ট্রোল করবে
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1B5E20)
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp),
        colors = colors,
        singleLine = true
    )
}



@Composable
fun textFieldColors(primary: Color, divider: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = primary,
    unfocusedBorderColor = divider,
    focusedLabelColor = primary,
    focusedTextColor = primary,
    unfocusedTextColor = divider
)

