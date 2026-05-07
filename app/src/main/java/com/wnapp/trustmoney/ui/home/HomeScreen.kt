package com.wnapp.trustmoney.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wnapp.trustmoney.ui.navigation.Screen
import kotlinx.coroutines.launch
import com.wnapp.trustmoney.ui.theme.BrandGreen
import com.wnapp.trustmoney.ui.theme.LightBg
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.wnapp.trustmoney.R
import com.wnapp.trustmoney.data.local.MyCurrency
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.model.TransactionModel
import com.wnapp.trustmoney.ui.theme.TBL_Green_Dark
import com.wnapp.trustmoney.viewmodel.AppViewModel
import com.wnapp.trustmoney.viewmodel.AuthViewModel
import java.io.File

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sm = remember { SessionManager(context) }
    val userId = remember { sm.getUserId() }
    val factory = remember { AppViewModel.AppViewModelFactory(context.applicationContext as Application) }

    val authVM: AuthViewModel = viewModel(factory = factory)
    val appVM: AppViewModel = viewModel(factory = factory)

    // রিসোর্স থেকে স্ট্রিংগুলো গেট করা
    val strSearching = stringResource(id = R.string.searching)
    val strHomeClicked = stringResource(id = R.string.home_clicked)
    val strOpeningLocation = stringResource(id = R.string.opening_location)
    val strLoggingOut = stringResource(id = R.string.logging_out)
    val strOpeningNotifications = stringResource(id = R.string.opening_notifications)
    val strOpeningQR = stringResource(id = R.string.opening_qr_scanner)
    val strAccOverview = stringResource(id = R.string.account_overview)
    val strRecentTrans = stringResource(id = R.string.recent_transactions)

    LaunchedEffect(Unit) {
        appVM.loadBanners()
        appVM.fetchWallet(userId)
        appVM.fetchTransactionHistory(userId)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val fullName = sm.getFullName()
    var showNotice by remember { mutableStateOf(true) }

    var showQRCode by remember { mutableStateOf(false) }
    if (showQRCode) {
        QRCodeDialog(
            qrData = stringResource(id = R.string.qr_data_label),
            onDismiss = { showQRCode = false }
        )
    }

    if (showNotice) {
        NoticeDialog(
            title = stringResource(id = R.string.notice_title),
            message = stringResource(id = R.string.notice_message),
            onDismiss = { showNotice = false }
        )
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DashboardDrawerContent(navController = navController, context= context, onClose = { scope.launch { drawerState.close() } })
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearch = { showToast(strSearching) },
                    onNotification = {
                        showToast(strOpeningNotifications)
                        navController.navigate(Screen.Notification.route)
                    },
                    onLogout = {
                        showToast(strLoggingOut)
                        authVM.logoutUser(context)
                        navController.navigate(Screen.Auth.route)
                    }
                )
            },
            bottomBar = {
                HomeBottomAppBar(
                    onHomeClick = { showToast(strHomeClicked) },
                    onLocationClick = {
                        showToast(strOpeningLocation)
                        val lat = 51.470543529142006
                        val lon = -0.3679932923594658
                        openMapLocation(context, lat, lon, "Trust Money Head Office")
                    },
                    onSwipeUp = { showBottomSheet = true }
                )
            },
            floatingActionButton = {
                CentralQrButton(onClick = {
                    showToast(strOpeningQR)
                    showQRCode = true
                })
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightBg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Column {
                            ProfileSection(userName = fullName, context, appVM)
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                        CategoryTabs(
                            selectedTab = selectedTab,
                            onTabSelected = {
                                selectedTab = it
                                showToast("Tab $it Selected")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OverviewHeader(title = strAccOverview)
                    TabContentCard(selectedTab = selectedTab, navController = navController, appVM)
                    SendMoneySection(navController = navController, appVM)
                    OverviewHeader(title = strRecentTrans)
                    TransactionPreviewCard(appVM)
                    Spacer(modifier = Modifier.height(130.dp))
                }
                AnimatedSwipeIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-15).dp),
                    onClick = { showBottomSheet = true }
                )
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    QuickActionsMenu(navController, appVM)
                }
            }
        }
    }
}
@Composable
fun HomeBottomAppBar(onHomeClick: () -> Unit, onLocationClick: () -> Unit, onSwipeUp: () -> Unit) {
    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 15.dp,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.pointerInput(Unit) {
            detectVerticalDragGestures { _, dragAmount ->
                if (dragAmount < -15) onSwipeUp()
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).clickable { onHomeClick() }.padding(top = 8.dp, bottom = 4.dp)
            ) {
                Icon(Icons.Default.Home, null, tint = BrandGreen)
                Text("Home", color = BrandGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).clickable { onLocationClick() }.padding(top = 8.dp, bottom = 4.dp)
            ) {
                Icon(Icons.Default.LocationOn, null, tint = Color.LightGray)
                Text("Location", color = Color.LightGray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun AnimatedSwipeIndicator(modifier: Modifier, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "SwipeUp")

    // animateValue এর জন্য typeConverter স্পষ্টভাবে উল্লেখ করা হয়েছে
    val offsetY by infiniteTransition.animateValue<Dp, AnimationVector1D>(
        initialValue = 0.dp,
        targetValue = (-8).dp,
        typeConverter = Dp.VectorConverter, // এখানে 'type' এর বদলে 'typeConverter' হবে
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "IconOffset"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Swipe up for Quick Actions", color = Color.Gray.copy(0.8f), fontSize = 10.sp)
        Icon(
            Icons.Default.KeyboardDoubleArrowUp,
            null,
            tint = BrandGreen,
            modifier = Modifier
                .size(40.dp)
                .offset(y = offsetY)
        )
    }
}


@Composable
fun CentralQrButton(onClick: () -> Unit) {
    // রিসোর্স থেকে স্ট্রিং গেট করা
    val strScanQr = stringResource(id = R.string.scan_qr)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = 85.dp) // আপনার ডিজাইন অনুযায়ী অফসেট বজায় রাখা হয়েছে
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = BrandGreen, // আপনার অ্যাপের ব্র্যান্ড গ্রিন কালার ব্যবহার করা হয়েছে
            contentColor = Color.White,
            modifier = Modifier
                .size(65.dp)
                .border(4.dp, Color.White, CircleShape)
                .shadow(8.dp, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = strScanQr, // অ্যাক্সেসিবিলিটির জন্য স্ট্রিং রিসোর্স ব্যবহার
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = strScanQr,
            color = BrandGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun ProfileSection(userName: String, context: Context, appVM: AppViewModel) {
    // ১. প্রোফাইল পিকচার স্টেট
    val profilePictureUri by remember {
        mutableStateOf<Uri?>(
            File(context.filesDir, "profile_picture.jpg").let { file ->
                if (file.exists()) Uri.fromFile(file) else null
            }
        )
    }

    var isBalanceVisible by remember { mutableStateOf(false) }
    val balance = appVM.wallet?.balance ?: "0.00"
    val currency = MyCurrency(context).getCurrencyName()

    // background color এর পরিবর্তে SVG ব্যবহার করার জন্য Box ব্যবহার করা হয়েছে
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // আপনার ডিজাইন অনুযায়ী হাইট সেট করুন
    ) {
        // ২. SVG ব্যাকগ্রাউন্ড ইমেজ
        Image(
            painter = painterResource(id = R.drawable.bg_screen), // এখানে আপনার SVG ফাইলের নাম দিন
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds, // পুরো এরিয়া জুড়ে দেখানোর জন্য
            // SVG এর ওপর ব্র্যান্ড কালারের একটি হালকা আভা দিতে চাইলে কালার ফিল্টার ব্যবহার করতে পারেন
            colorFilter = ColorFilter.tint(BrandGreen.copy(alpha = 0.95f), BlendMode.SrcOver)
        )

        // ৩. মেইন কন্টেন্ট (Column)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // প্রোফাইল পিকচার সেকশন
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier
                    .size(85.dp)
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                    .padding(4.dp)
            ) {
                if (profilePictureUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profilePictureUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(15.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // নাম এবং স্বাগতম বার্তা
            Text("Welcome to Trust Money", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            Text(userName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // ব্যালেন্স বাটন
            Surface(
                onClick = {
                    if (!isBalanceVisible) {
                        appVM.fetchWallet(userId = SessionManager(context).getUserId())
                    }
                    isBalanceVisible = !isBalanceVisible
                },
                shape = RoundedCornerShape(50.dp),
                color = Color.White.copy(alpha = 0.2f), // SVG এর ওপর একটু স্পষ্ট দেখানোর জন্য আলফা বাড়ানো হয়েছে
                modifier = Modifier.height(42.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    AnimatedContent(
                        targetState = isBalanceVisible,
                        label = "BalanceToggle"
                    ) { visible ->
                        Text(
                            text = if (visible) "$balance $currency" else "Tap for Balance",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).shadow(10.dp, RoundedCornerShape(16.dp)).background(Color.White, RoundedCornerShape(16.dp)).padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        val categories = listOf(CategoryItemData("Accounts", Icons.Default.AccountBalance), CategoryItemData("Cards", Icons.Default.CreditCard), CategoryItemData("Loans", Icons.Default.AccountBalanceWallet), CategoryItemData("Schemes", Icons.Default.Layers))
        categories.forEachIndexed { index, item -> CategoryTabItem(data = item, isSelected = selectedTab == index, onClick = { onTabSelected(index) }) }
    }
}

@Composable
fun TabContentCard(selectedTab: Int, navController: NavController, appVM: AppViewModel) {
    when (selectedTab) {
        0 -> {CardOne(navController = navController, appVM )}
        1 -> {CardTwo(navController = navController)}
        2 -> {CardThree(navController = navController)}
        3 -> {CardFour(navController = navController)}
    }
}

@Composable
fun CardOne(navController: NavController, appVM: AppViewModel) {
    val context = LocalContext.current
    val fullName = SessionManager(context).getFullName() ?: "CARD HOLDER"

    // ডাটা ফরম্যাটিং
    val rawCardNumber = "5896486474445712"
    val formattedCardNumber = rawCardNumber.chunked(4).joinToString("  ")
    val issueDate = "05/24"
    val expiryDate = "05/29"

    val embossedGold = Color(0xFFFFD700)
    val shadowColor = Color.Black.copy(alpha = 0.6f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // ব্যাকগ্রাউন্ড ইমেজ
            Image(
                painter = painterResource(id = R.drawable.green_card),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // কার্ডের ভেতরের কন্টেন্ট
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // ১. উপরের অংশ: চিপ বা লোগো (ঐচ্ছিক স্পেসার)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                }

                // ২. মাঝের অংশ: কার্ড নম্বর
                Text(
                    text = formattedCardNumber,
                    style = TextStyle(
                        color = embossedGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        shadow = Shadow(color = shadowColor, offset = Offset(2f, 4f), blurRadius = 2f)
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                )

                // ৩. নিচের অংশ: তারিখ এবং নাম
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Issue Date
                        DateItem(label = "ISSUE", date = issueDate, gold = embossedGold, shadow = shadowColor)
                        Spacer(modifier = Modifier.width(30.dp))
                        // Expiry Date
                        DateItem(label = "EXPIRY", date = expiryDate, gold = embossedGold, shadow = shadowColor)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // কার্ড হোল্ডার নেম
                    Text(
                        text = fullName.uppercase(),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            shadow = Shadow(color = shadowColor, offset = Offset(2f, 2f))
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DateItem(label: String, date: String, gold: Color, shadow: Color) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = date,
            style = TextStyle(
                color = gold.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(color = shadow, offset = Offset(1f, 2f))
            )
        )
    }
}


@SuppressLint("RememberReturnType")

@Composable

fun _CardOne(navController: NavController, appVM: AppViewModel) {

    val context = LocalContext.current

    val fullName = SessionManager(context).getFullName()

    val strAccountNumber = "0023 1245 6789" // Cleaned up for a premium card look



    Card(

        modifier = Modifier

            .fillMaxWidth()

            .padding(horizontal = 16.dp, vertical = 8.dp)

            .height(200.dp),

        shape = RoundedCornerShape(20.dp),

        elevation = CardDefaults.cardElevation(8.dp)

    ) {

        Box(modifier = Modifier.fillMaxSize()) {

// 1. Background Image

            Image(

                painter = painterResource(id = R.drawable.green_card),

                contentDescription = null,

                modifier = Modifier.fillMaxSize(),

                contentScale = ContentScale.FillBounds

            )



// 2. Content Layer

            Column(

                modifier = Modifier

                    .fillMaxSize()

                    .padding(24.dp),

                verticalArrangement = Arrangement.Bottom // Aligns all content to the bottom

            ) {

// Card Number (Positioned above the name)




                Spacer(modifier = Modifier.height(4.dp))



// Card Holder Name (Positioned at the very bottom left)

                Text(

                    text = fullName.uppercase(),

                    color = Color.White,

                    fontSize = 16.sp,

                    fontWeight = FontWeight.Bold,

                    letterSpacing = 1.5.sp

                )

            }

        }

    }

}

@Composable
fun CardTwo(navController: NavController) {
    val context = LocalContext.current
    val status = SessionManager(context).getUserStatus()

    // রিসোর্স থেকে স্ট্রিং গেট করা
    val strAddCards = stringResource(id = R.string.add_cards)
    val strTapToAdd = stringResource(id = R.string.tap_to_add_cards)

    if (status == "agent") {
        DuelCard()
    } else {
        EmptyCard(
            text1 = strAddCards,
            text2 = strTapToAdd,
            navController = navController
        )
    }
}
@Composable
fun CardThree(navController: NavController) {
    // রিসোর্স থেকে স্ট্রিং গেট করা
    val strApplyLoan = stringResource(id = R.string.apply_for_loan)

    EmptyCard(
        text1 = strApplyLoan,
        text2 = "",
        navController = navController
    )
}

@Composable
fun CardFour(navController: NavController) {
    // রিসোর্স থেকে স্ট্রিং গেট করা
    val strApplyScheme = stringResource(id = R.string.apply_for_scheme)

    EmptyCard(
        text1 = strApplyScheme,
        text2 = "",
        navController = navController
    )
}

@Composable
fun EmptyCard(text1: String, text2: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFFF0F0F0),
                    radius = 150f,
                    center = Offset(size.width * 0.9f, size.height * 0.2f)
                )
                drawCircle(
                    color = Color(0xFFF5F5F5),
                    radius = 200f,
                    center = Offset(size.width * 0.1f, size.height * 0.8f)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = BrandGreen,
                    modifier = Modifier
                        .size(56.dp)
                        .clickable { navController.navigate(Screen.PackageScreen.route) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_label),
                        tint = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = text1, color = BrandGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = text2, color = BrandGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DuelCard() {
    val cardHolderName = SessionManager(LocalContext.current).getFullName() // সেশন থেকে ইউজারের নাম নেওয়া হচ্ছে
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(210.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.visa_card_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = cardHolderName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(32.dp)
            )
        }
    }
}


@Composable
fun SendMoneySection(navController: NavController, appVM: AppViewModel) {
    // বাটন ডেটা লিস্ট
    val paymentMethods = listOf(
        Pair("Bkash", R.drawable.ic_bkash), // আপনার প্রোজেক্টে এই নামে ড্রয়াবল থাকতে হবে
        Pair("Nagad", R.drawable.ic_nagad),
        Pair("Upay", R.drawable.ic_upay),
        Pair("Rocket", R.drawable.ic_rocket)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // সুন্দর হেডলাইন
        OverviewHeader(title = "Send Money with...")

        Spacer(modifier = Modifier.height(12.dp))

        // বাটন রো
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            paymentMethods.forEach { method ->
                PaymentIconCard(
                    name = method.first,
                    iconRes = method.second,
                    onClick = {
                        navController.navigate(Screen.SendMoney.passAmount(""))
                    }
                )
            }

        }
    }
}

@Composable
fun PaymentIconCard(name: String, iconRes: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            // নতুন ripple() API ব্যবহার করা হয়েছে
            indication = ripple(
                bounded = false,
                radius = 32.dp, // প্রয়োজন অনুযায়ী অ্যাডজাস্ট করতে পারেন
                color = Color.Gray // ব্র্যান্ড কালার বা থিম কালার দিতে পারেন
            ),
            onClick = onClick
        )
    ) {
        Card(
            modifier = Modifier.size(65.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}
@Composable
fun TransactionPreviewCard(appVM: AppViewModel) {
    val transactions = appVM.transactionList // ভিউমডেল থেকে লেনদেনের তালিকা নেওয়া হচ্ছে

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (transactions.isNotEmpty()) {
            Column {
                transactions.forEach { transaction ->
                    TransactionPreviewItem(transaction)

                    if (transaction != transactions.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_transactions),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun TransactionPreviewItem(transaction: TransactionModel) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = Color(0xFFF0F0F0), modifier = Modifier.size(45.dp)) {
            Icon(Icons.Default.ArrowUpward, null, modifier = Modifier.padding(12.dp), tint = Color.Red)
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column(modifier = Modifier.weight(1f)) {
            transaction.description?.let { Text(it, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            Text(transaction.createdAt, color = Color.Gray, fontSize = 11.sp)
        }
        Text(transaction.amount.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
@Composable
fun CategoryTabItem(data: CategoryItemData, isSelected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { onClick() }.padding(4.dp)) {
        Box(modifier = Modifier.size(50.dp).background(if (isSelected) BrandGreen.copy(0.1f) else Color(0xFFF8F8F8), CircleShape), contentAlignment = Alignment.Center) {
            Icon(data.icon, null, tint = if (isSelected) BrandGreen else Color.Gray, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(data.label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) BrandGreen else Color.DarkGray)
    }
}
// ১. HomeTopBar আপডেট
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onMenuClick: () -> Unit, onSearch: () -> Unit, onNotification: () -> Unit, onLogout: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.app_name_label), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = { IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, null, tint = Color.White) } },
        actions = {
            IconButton(onClick = onNotification) { Icon(Icons.Default.NotificationsNone, null, tint = Color.White) }
            IconButton(onClick = onLogout) { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.White) }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrandGreen)
    )
}

// ২. OverviewHeader আপডে

@Composable
fun OverviewHeader(title: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 16.sp)
        Text(stringResource(id = R.string.see_all), color = BrandGreen, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { })
    }
}

// ১. DashboardDrawerContent আপডেট
@Composable
fun DashboardDrawerContent(navController: NavController, context: Context, onClose: () -> Unit) {
    val profilePictureUri by remember {
        mutableStateOf<Uri?>(
            File(context.filesDir, "profile_picture.jpg").let { file ->
                if (file.exists()) Uri.fromFile(file) else null
            }
        )
    }

    ModalDrawerSheet(drawerContainerColor = Color.White, modifier = Modifier.width(400.dp)) {
        Box(modifier = Modifier.fillMaxWidth().background(BrandGreen).padding(30.dp).align(alignment = Alignment.CenterHorizontally)) {
            Column {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier
                        .size(85.dp)
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .padding(4.dp)
                ) {
                    if (profilePictureUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profilePictureUri),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Person, null, modifier = Modifier.padding(15.dp), tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(stringResource(id = R.string.app_name_label), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                // ডাইনামিক আইডি ফরম্যাটিং
                Text(stringResource(id = R.string.user_id_label, "554872"), color = Color.White.copy(0.7f), fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        DrawerMenuItem(Icons.Default.Settings, stringResource(id = R.string.profile_settings)) {
            onClose()
            navController.navigate(Screen.ProfileSetting.route)
        }
        DrawerMenuItem(Icons.Default.History, stringResource(id = R.string.statement)) {
            onClose()
            navController.navigate(Screen.TransactionHistoryScreen.route)
        }
        DrawerMenuItem(Icons.Default.HelpCenter, stringResource(id = R.string.support)) {
            onClose()
            navController.navigate(Screen.Support.route)
        }
        DrawerMenuItem(Icons.Default.Verified, "Verify your account") {
            onClose()
            navController.navigate(Screen.UploadDocument.route)
        }
        DrawerMenuItem(Icons.Default.Pin, stringResource(id = R.string.change_pin)) {
            onClose()
            navController.navigate(Screen.PinEnter.route)
        }

        HorizontalDivider(modifier = Modifier.padding(20.dp))

        DrawerMenuItem(Icons.AutoMirrored.Filled.Logout, stringResource(id = R.string.logout)) {
            onClose()
            navController.navigate(Screen.Auth.route)
        }
    }
}

// ২. NoticeDialog আপডেট
@Composable
fun NoticeDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandGreen
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_desc),
                        tint = Color.Gray
                    )
                }
            }
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}


@Composable
fun QuickActionsMenu(navController: NavController, appVM: AppViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.quick_actions),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TBL_Green_Dark
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickActionButton(Icons.Default.AttachMoney, stringResource(id = R.string.add_money_label)) {
                navController.navigate(Screen.AddBalance.passAmount("0"))
            }
            QuickActionButton(Icons.Default.Send, stringResource(id = R.string.send_money_label)) {
                val currentBalance = appVM.wallet?.balance?.toDoubleOrNull() ?: 0.0
                if (currentBalance > 0) navController.navigate(Screen.SendMoney.passAmount("0"))
                else navController.navigate(Screen.InsufficientBalance.route)
            }
            QuickActionButton(Icons.Default.PhoneIphone, stringResource(id = R.string.mobile_recharge_label)) {
                val currentBalance = appVM.wallet?.balance?.toDoubleOrNull() ?: 0.0
                if(currentBalance > 0) navController.navigate(Screen.MobileRecharge.passAmount("0"))
                else navController.navigate(Screen.InsufficientBalance.route)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickActionButton(Icons.Default.LocalOffer, stringResource(id = R.string.best_packages_label)) {
                navController.navigate(Screen.PackageScreen.route)
            }
            QuickActionButton(Icons.Default.History, stringResource(id = R.string.history_label)) {
                navController.navigate(Screen.TransactionHistoryScreen.route)
            }
            QuickActionButton(Icons.Default.ReceiptLong, stringResource(id = R.string.utility_bill_label)) {
                val currentBalance = appVM.wallet?.balance?.toDoubleOrNull() ?: 0.0
                if(currentBalance > 0) navController.navigate(Screen.MobileRechargeMethodSelectionScreen.route)
                else navController.navigate(Screen.InsufficientBalance.route)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickActionButton(Icons.Default.DocumentScanner, stringResource(id = R.string.company_docs_label)) {
                navController.navigate(Screen.CompanyDocs.route)
            }
            QuickActionButton(Icons.Default.ManageHistory, stringResource(id = R.string.request_history)) {
                navController.navigate(Screen.RequestHistory.route)
            }
            QuickActionButton(Icons.Default.SupportAgent, stringResource(id = R.string.support_label)) {
                navController.navigate(Screen.Support.route)
            }
        }
        // ... (বাকি ইমেজ স্লাইডার অংশ অপরিবর্তিত থাকবে)
        // ... (আগের Row গুলোর পর থেকে শুরু)

        Spacer(modifier = Modifier.height(25.dp)) // মেনু এবং স্লাইডারের মাঝে গ্যাপ

        // ইমেজ স্লাইডার সেকশন
        val combinedImages = remember(appVM.bannerList) {
            // ভিউমডেলের bannerList থেকে শুধুমাত্র URL গুলো আলাদা করা হচ্ছে
            appVM.bannerList.map { it.bannerUrl }
        }

        if (combinedImages.isNotEmpty()) {
            // যদি লিস্টে ইমেজ থাকে তবেই স্লাইডারটি দেখানো হবে
            ModernImageSlider(images = combinedImages)
        } else {
            // ডাটা লোড না হওয়া পর্যন্ত বা খালি থাকলে সামান্য স্পেস
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
@Composable
fun QuickActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(90.dp).clickable { onClick() }) {
        Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(15.dp), color = Color(0xFFF5F5F5)) {
            Icon(icon, null, tint = BrandGreen, modifier = Modifier.padding(18.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 11.sp, color = TBL_Green_Dark, textAlign = TextAlign.Center, lineHeight = 14.sp)
    }
}
@Composable
fun DrawerMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text = label) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, null, tint = BrandGreen) },
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernImageSlider(images: List<Any>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // ১. ইমেজ স্লাইডার কন্টেইনার
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 1f), // 3:1 রেশিও সেট করা হয়েছে
            shape = RoundedCornerShape(16.dp), // মডার্ন রাউন্ডেড কর্নার
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = "Slider Image $page",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // ছবিটিকে ফ্রেমের সাথে ফিট করার জন্য
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ২. স্লাইডার ইন্ডিকেটর (নিচে ছোট ডট)
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF008346) else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun QRCodeDialog(
    qrData: String, // এখানে আপনার ইউজার আইডি বা ওয়ালেট অ্যাড্রেস আসবে
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // QR কোড সেকশন
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // এখানে আপনার QR জেনারেটর ফাংশন কল করবেন
                    // উদাহরণ হিসেবে একটি প্লেসহোল্ডার ইমেজ দেওয়া হলো
                    Image(
                        painter = painterResource(id = R.drawable.sample_qr), // আপনার QR কোড রিসোর্স
                        contentDescription = "QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // নিচের টেক্সট
                Text(
                    text = "QR CODE SCAN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

fun openMapLocation(context: Context, latitude: Double, longitude: Double, label: String = "Location") {
    // geo:latitude,longitude ফরম্যাটে ইউআরআই তৈরি করা
    // q=... অংশটি ম্যাপে একটি পিন (Marker) দেখাবে
    val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

    // গুগল ম্যাপস সরাসরি ওপেন করার জন্য প্যাকেজ সেট করা (ঐচ্ছিক)
    intent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // যদি গুগল ম্যাপস ইন্সটল না থাকে তবে ব্রাউজার বা অন্য ম্যাপ অ্যাপ ট্রাই করবে
        val untitledIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(untitledIntent)
    }
}

data class CategoryItemData(val label: String, val icon: ImageVector)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        HomeScreen(navController = navController)
    }
}