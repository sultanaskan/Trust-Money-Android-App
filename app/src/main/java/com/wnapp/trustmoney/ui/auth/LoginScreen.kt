package com.wnapp.trustmoney.ui.auth


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wnapp.trustmoney.ui.theme.TBL_Divider_Gray
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark
import com.wnapp.trustmoney.ui.theme.TBL_Soft_Green_Bg
import com.wnapp.trustmoney.ui.theme.TextGray
import com.wnapp.trustmoney.ui.theme.White
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.RequestFormData
import com.wnapp.trustmoney.ui.components.OtpVerificationDialog
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.Black

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(navController: NavController) {
    // ট্যাব স্টেট (0 = লগইন, 1 = নিউ ইউজার)
    var selectedTab by remember { mutableStateOf(0) }
    var subTabState by remember { mutableStateOf(0) }
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize().background(White)) {

        // ১. ক্যানভাসের পরিবর্তে ব্যাকগ্রাউন্ড ইমেজ যুক্ত করা হয়েছে
        Image(
            painter = painterResource(id = R.drawable.bg_screen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds // ইমেজটি পুরো স্ক্রিন জুড়ে থাকবে
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            // ২. লোগো
            Image(
                painter = painterResource(id = R.drawable.ic_trust_money_logo),
                contentDescription = null,
                modifier = Modifier.width(170.dp).height(85.dp)
            )

            Spacer(modifier = Modifier.height(45.dp))

            // ৩. প্রফেশনাল ট্যাব সুইচ
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, TBL_Green_Dark, RoundedCornerShape(26.dp))
                    .padding(2.dp)
            ) {
                TabButton("Login", selectedTab == 0, Modifier.weight(1f)) { selectedTab = 0 }
                TabButton("New user Request", selectedTab == 1, Modifier.weight(1.4f)) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // ৪. কন্টেন্ট সুইচিং
            androidx.compose.animation.AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    androidx.compose.animation.fadeIn() with androidx.compose.animation.fadeOut()
                }, label = ""
            ) { targetTab ->
                if (targetTab == 0) {
                    LoginForm(navController = navController)
                } else {
                    RegistrationForm(
                        selectedSubTab = subTabState,
                        onSubTabSelected = { subTabState = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ৫. নিচের স্থায়ী অংশ
            if (selectedTab == 0) {
                Text(
                    "Forgot User ID, Password & Unlock",
                    color = TBL_Green_Dark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate(Screen.ForgotPass.route)}
                )
            }

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
fun LoginForm(navController: NavController) {
    // স্টেট ম্যানেজমেন্ট
    var creds by remember { mutableStateOf(LoginCreds()) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }

    Column {
        // User ID Field
        OutlinedTextField(
            value = creds.userId,
            onValueChange = { creds = creds.copy(userId = it) },
            label = { Text("TBL User ID", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Person, null, tint = TBL_Green_Dark) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                focusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Password Field
        OutlinedTextField(
            value = creds.password,
            onValueChange = { creds = creds.copy(password = it) },
            label = { Text("Password", color = TextGray) },
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
                focusedTextColor = Color.Black
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Login Button
        Button(
            onClick = {
                // ১. এখানে প্রথমে API কল হবে সার্ভারে ডাটা (creds) পাঠিয়ে
                // ২. সার্ভার যদি সাকসেস দেয়, তবেই কেবল ডায়ালগ দেখাবো
                if (creds.userId.isNotEmpty() && creds.password.isNotEmpty()) {
                    showOtpDialog = true
                    navController.navigate(Screen.Dashboard.route)
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

    // ৩. OTP ডায়ালগ কল করা
    if (showOtpDialog) {
        OtpVerificationDialog(
            onDismiss = { showOtpDialog = false },
            onVerify = { otpCode ->
                // এখানে OTP ভেরিফাই করে হোম স্ক্রিনে যাওয়ার লজিক
                println("Verifying OTP for User: ${creds.userId} with Code: $otpCode")
                showOtpDialog = false
            }
        )
    }
}




@Composable
fun RegistrationForm(selectedSubTab: Int, onSubTabSelected: (Int) -> Unit) {
    var formData by remember { mutableStateOf(RequestFormData()) }
    var showOtpDialog by remember { mutableStateOf(false) }
    Column {
        // --- সাব-ট্যাব রো (Account, Credit, Prepaid) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SubTabItem("Account No", selectedSubTab == 0, Modifier.weight(1f)) { onSubTabSelected(0) }
            SubTabItem("Credit Card", selectedSubTab == 1, Modifier.weight(1f)) { onSubTabSelected(1) }
            SubTabItem("Prepaid Card", selectedSubTab == 2, Modifier.weight(1f)) { onSubTabSelected(2) }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // --- সাব-ট্যাব অনুযায়ী আলাদা আলাদা ইনপুট ফিল্ড ---
        when (selectedSubTab) {
            0 -> AccountRequestForm(formData){formData = it}
            1 -> CreditCardRequestForm(formData){formData = it}
            2 -> PrepaidCardRequestForm(formData){formData = it}
        }
        Button(
            onClick = {
                // এখানে আপনার সাবমিট লজিক বা ভ্যালিডেশন লিখুন
                showOtpDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TBL_Green_Dark,
                contentColor = White
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Submit Request",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Rounded.Send, // সাবমিটের জন্য Send বা ArrowForward আইকন
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ৪. এখানে ডায়ালগ কল করুন (এটি Column-এর বাইরে থাকাই ভালো)
        if (showOtpDialog) {
            OtpVerificationDialog(
                onDismiss = { showOtpDialog = false },
                onVerify = { otp ->
                    // এখানে OTP ভেরিফিকেশন লজিক
                    showOtpDialog = false // ভেরিফিকেশন শেষে বন্ধ করে দিন
                }
            )
        }
    }
}

@Composable
fun SubTabItem(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(45.dp) // উচ্চতা কিছুটা বাড়ানো হয়েছে যাতে আন্ডারলাইনটি নিচে স্পষ্ট থাকে
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // ক্লিক করার সময় ডিফল্ট গ্রে ইফেক্ট না চাইলে এটি রাখতে পারেন
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                // সিলেক্টেড থাকলে ব্র্যান্ড গ্রিন, না থাকলে কালো
                color = if (isSelected) TBL_Green_Dark else Color.Black,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            // যদি সিলেক্টেড থাকে, তবেই নিচে সবুজ লাইনটি দেখাবে
            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp)) // টেক্সট এবং লাইনের মাঝে গ্যাপ
                Box(
                    modifier = Modifier
                        .width(40.dp) // আন্ডারলাইনের প্রস্থ (আপনি চাইলে এটি পরিবর্তন করতে পারেন)
                        .height(2.dp) // আন্ডারলাইনের পুরুত্ব
                        .background(TBL_Green_Dark, shape = RoundedCornerShape(1.dp))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountRequestForm(
    formData: RequestFormData,
    onDataChange: (RequestFormData) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        OutlinedTextField(
            value = formData.uniqueField,
            onValueChange = {onDataChange(formData.copy(uniqueField = it))},
            label = { Text("Account Number", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.AccountBalance,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        CommonRequestFields(formData, onDataChange)
    }
}


@Composable
fun CreditCardRequestForm(
    formData: RequestFormData,
    onDataChange: (RequestFormData) -> Unit
) {
    Column {
        OutlinedTextField(
            value = formData.uniqueField,
            onValueChange = {onDataChange(formData.copy(uniqueField = it))},
            label = { Text("Credit Card", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CreditCard,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        CommonRequestFields(formData, onDataChange)
    }
}

@Composable
fun PrepaidCardRequestForm(
    formData: RequestFormData,
    onDataChange: (RequestFormData) -> Unit
) {
    Column {
        OutlinedTextField(
            value = formData.uniqueField,
            onValueChange = {onDataChange(formData.copy(uniqueField = it))},
            label = { Text("Prepaid Card", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.AddCard,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        CommonRequestFields(formData, onDataChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonRequestFields(
    formData: RequestFormData,
    onValueChange: (RequestFormData) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // ১. ইমেইল

        var expanded by remember { mutableStateOf(false) }
        val securityQuestions = listOf(
            "What is your pet's name?",
            "What is your mother's maiden name?",
            "What was your first school?",
            "What is your favorite city?"
        )
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }



        OutlinedTextField(
            value = formData.email,
            onValueChange = {onValueChange(formData.copy(email = it))},
            label = { Text("Email Address", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        OutlinedTextField(
            value = formData.phone,
            onValueChange = {onValueChange(formData.copy(phone = it))},
            label = { Text("Phone Number", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.PhoneAndroid,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark
            ),
            singleLine = true
        )
        OutlinedTextField(
            value = formData.name,
            onValueChange = {onValueChange(formData.copy(name = it))},
            label = { Text("Your Name", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark
            ),
            singleLine = true
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = formData.securityQuestion,
                onValueChange = {onValueChange(formData.copy(securityQuestion = it))},
                readOnly = true, // ইউজার নিজে টাইপ করতে পারবে না, শুধু সিলেক্ট করবে
                label = { Text("Security Question", color = TextGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(), // মেনুটিকে টেক্সট ফিল্ডের সাথে অ্যাঙ্কর করে
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Security, // সিকিউরিটি আইকন
                        contentDescription = null,
                        tint = TBL_Green_Dark
                    )
                },
                trailingIcon = {
                    // ড্রপডাউন অ্যারো আইকন
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TBL_Green_Dark,
                    unfocusedBorderColor = TBL_Divider_Gray,
                    focusedLabelColor = TBL_Green_Dark,
                    focusedTextColor = TBL_Green_Dark,
                    unfocusedTextColor = TBL_Divider_Gray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(White)
            ) {
                securityQuestions.forEach { question ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = question,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        onClick = {
                            // ১. ডাটা আপডেট করার সঠিক উপায়:
                            onValueChange(formData.copy(securityQuestion = question))
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        colors = MenuDefaults.itemColors(
                            // টেক্সট কালার
                            textColor = Color.Black,
                            leadingIconColor = TBL_Green_Dark,
                            trailingIconColor = TBL_Green_Dark,
                            disabledTextColor = Color.Gray
                          )
                    )
                }
            }
        }

        OutlinedTextField(
            value = formData.securityAnswer,
            onValueChange = {onValueChange(formData.copy(securityAnswer = it))},
            label = { Text("Security Ans", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            // বাম দিকে আইকন যোগ করা হলো
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.QuestionAnswer,
                    contentDescription = null,
                    tint = TBL_Green_Dark // আপনার ব্র্যান্ড কালার
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        OutlinedTextField(
            value = formData.password,
            onValueChange = { onValueChange(formData.copy(password = it)) },
            label = { Text("Password", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock, // পাসওয়ার্ডের জন্য Lock আইকন বেশি মানানসই
                    contentDescription = null,
                    tint = TBL_Green_Dark
                )
            },
            // ২. ডান দিকে আইকন (Password Toggle) যোগ করা হলো
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Outlined.Visibility
                else
                    Icons.Outlined.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = TBL_Green_Dark)
                }
            },
            // ৩. ভিজ্যুয়াল ট্রান্সফরমেশন (স্টার বা নরমাল টেক্সট দেখানোর লজিক)
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

            // ৪. কিবোর্ড টাইপ পাসওয়ার্ড সেট করা (সিকিউরিটির জন্য)
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        OutlinedTextField(
            value = formData.confirmPassword,
            onValueChange = { onValueChange(formData.copy(confirmPassword = it)) },
            label = { Text("Confirm Password", color = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LockReset, // কনফার্ম পাসওয়ার্ডের জন্য ভিন্ন বা একই আইকন ব্যবহার করতে পারেন
                    contentDescription = null,
                    tint = TBL_Green_Dark
                )
            },
            // ডান দিকে পাসওয়ার্ড টগল আইকন
            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    Icons.Outlined.Visibility
                else
                    Icons.Outlined.VisibilityOff

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = TBL_Green_Dark)
                }
            },
            // ভিজ্যুয়াল ট্রান্সফরমেশন লজিক
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),

            // কিবোর্ড টাইপ পাসওয়ার্ড সেট করা
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TBL_Green_Dark,
                unfocusedBorderColor = TBL_Divider_Gray,
                focusedLabelColor = TBL_Green_Dark,
                focusedTextColor = TBL_Green_Dark,
                unfocusedTextColor = TBL_Divider_Gray
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(30.dp)) // ফিল্ড এবং বাটনের মাঝে গ্যাপ



    }
}



@Composable
fun LoginActionButtons() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = {},
            modifier = Modifier.weight(1f).height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TBL_Green_Dark),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color= White)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(Icons.Rounded.ArrowForward, null, tint = White)
            }
        }
        Spacer(modifier = Modifier.width(15.dp))
        OutlinedIconButton(
            onClick = {},
            modifier = Modifier.size(55.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, TBL_Divider_Gray)
        ) {
            Icon(Icons.Rounded.Face, null, tint = TBL_Green_Dark, modifier = Modifier.size(32.dp))
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
        border = BorderStroke(1.dp, TBL_Green_Dark),
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
                tint = TBL_Green_Dark,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                color = TBL_Green_Dark,
                fontWeight = FontWeight.Bold,
                lineHeight = 14.sp
            )
        }
    }
}