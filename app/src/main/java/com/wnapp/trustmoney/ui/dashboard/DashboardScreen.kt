package com.wnapp.trustmoney.ui.dashboard // আপনার প্রজেক্টের প্যাকেজ অনুযায়ী এটি পরিবর্তন করুন

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.wnapp.trustmoney.ui.navigation.Screen
import com.wnapp.trustmoney.ui.theme.BrandGreen
import com.wnapp.trustmoney.ui.theme.White
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wnapp.trustmoney.R

// আপনার ব্র্যান্ড কালারসমূহ (যদি আলাদা ফাইলে থাকে তবে সেখান থেকে ইম্পোর্ট করুন)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(1) } // Default Cards selected
    val context = LocalContext.current
    var showScanner by remember { mutableStateOf(false) }
    // পারমিশন হ্যান্ডলার
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showScanner = true
        } else {
            Toast.makeText(context, "ক্যামেরা পারমিশন প্রয়োজন", Toast.LENGTH_SHORT).show()
        }
    }

    if (showScanner) {
        QrScannerDialog(
            onDismiss = { showScanner = false },
            onResult = { result ->
                showScanner = false
                // রেজাল্ট (যেমন: বিকাশ নাম্বার বা আইডি) নিয়ে কাজ করুন
                Toast.makeText(context, "Scanned: $result", Toast.LENGTH_LONG).show()
            }
        )
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DashboardDrawerContent() },
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectVerticalDragGestures { change, dragAmount ->
                if (dragAmount < -20) {
                    showBottomSheet = true
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                DashboardTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onLogout = { navController.navigate(Screen.Auth.route) }
                )
            },
            bottomBar = {
                DashboardBottomBar(
                    onHomeClick = {  navController.navigate(Screen.Dashboard.route) { popUpTo("home") { inclusive = true } } },
                    onLocationClick = {
                        // আপনার কাঙ্ক্ষিত লোকেশন (যেমন: উত্তরা বা গাজীপুর এর কোনো পয়েন্ট)
                        val lat = 23.8943 // উদাহরণ: টঙ্গী/উত্তরা এরিয়া
                        val lng = 90.3928
                        val label = "আমাদের প্রধান শাখা" // ম্যাপে এই নাম দেখাবে

                        val uri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(label)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, uri)

                        // অ্যাপ ট্রাই করো, না থাকলে ব্রাউজার
                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            val browserIntent = Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng"))
                            context.startActivity(browserIntent)
                        }
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton =  {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = {
                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            showScanner = true },
                        shape = CircleShape,
                        containerColor = BrandGreen,
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(65.dp)
                            // ৪. এই অফসেটটি বাটনকে ঠিক মাঝখানে 'ডক' (Dock) করবে
                            .offset(y = 80.dp)
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(32.dp))
                    }
                    // বাটনের নিচের টেক্সট (এটি বটম বারের লাইনের সাথে মিশে থাকবে)
                    Text(
                        "Qr Payment",
                        color = BrandGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(y = 80.dp)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5))
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ১. প্রোফাইল হেডার
                ProfileHeaderSection(userName = "MD HABIBUR RAHMAN")

                // ২. ক্যাটাগরি ট্যাব বাটন
                CategoryTabs(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

                // ৩. ডাইনামিক কন্টেন্ট
                TabContentArea(selectedTab)

                // ৪. ড্রাগেবল মেনু ইন্ডিকেটর
                Spacer(modifier = Modifier.height(20.dp))
                // ১. এনিমেশন ট্রানজিশন তৈরি

                val infiniteTransition = rememberInfiniteTransition(label = "iconTransition")
                val offsetAnim by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -5f, // কতটুকু উপরে উঠবে
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse // উপরে গিয়ে আবার নিচে নামবে
                    ),
                    label = "yOffset"
                )

                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowUp,
                        contentDescription = "Open Menu",
                        tint = BrandGreen,
                        modifier = Modifier
                            .size(30.dp)
                            .offset(y = offsetAnim.dp) // ৩. এখানে এনিমেশনটি অ্যাপ্লাই করুন
                    )
                }


            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = White
                ) {
                    QuickActionsMenu(navController = navController)
                }
            }
        }
    }
}












@Composable
fun LiveCurrencyConverter() {
    val rate = 37.2 // ১ QAR = ৩৭.২ BDT

    // স্টেট ম্যানেজমেন্ট
    var qarValue by remember { mutableStateOf("1") }
    var bdtValue by remember { mutableStateOf(rate.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF1F8F1), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // ১. উপরের বার: তথ্য প্রদর্শন
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // এখানে আপনার ড্রয়েবল ফোল্ডার থেকে পতাকার আইকন দিতে পারেন
                Text("🇶🇦", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Qatar", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }
            Text(
                "1 QAR = $rate BDT",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D40)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

        // ২. নিচের বার: লাইভ কনভার্সন ইনপুট
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // বাম পাশ: QAR ইনপুট
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🇶🇦", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("QAR", fontSize = 12.sp, color = Color.Gray)
                }
                TextField(
                    value = qarValue,
                    onValueChange = { input ->
                        qarValue = input
                        val converted = input.toDoubleOrNull()?.let { it * rate }
                        bdtValue = converted?.let { "%.2f".format(it) } ?: ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            // মাঝখানে কনভার্সন আইকন
            Icon(
                imageVector = Icons.Default.SyncAlt,
                contentDescription = null,
                tint = Color(0xFF004D40),
                modifier = Modifier.padding(horizontal = 8.dp).size(24.dp)
            )

            // ডান পাশ: BDT ইনপুট
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🇧🇩", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("BDT", fontSize = 12.sp, color = Color.Gray)
                }
                TextField(
                    value = bdtValue,
                    onValueChange = { input ->
                        bdtValue = input
                        val converted = input.toDoubleOrNull()?.let { it / rate }
                        qarValue = converted?.let { "%.2f".format(it) } ?: ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }
        }
    }
}


@Composable
fun QrScannerDialog(onDismiss: () -> Unit, onResult: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Scan QR Code",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ক্যামেরা প্রিভিউ হোল্ডার
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black)
                ) {
                    // এখানে ক্যামেরা লাইভ ভিউ আসবে (নিচে কোড দেওয়া আছে)
                    CameraPreviewView(onResult)

                    // স্ক্যানার বর্ডার ডিজাইন
                      ScannerOverlay()
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun ScannerOverlay() {
    // ১. স্ক্যানিং লাইনের অ্যানিমেশন তৈরি (উপর থেকে নিচে নামবে)
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val lineOffset by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lineOffset"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val boxSize = width * 0.7f // স্ক্যানার বক্সের সাইজ (স্ক্রিনের ৭০%)

        val left = (width - boxSize) / 2
        val top = (height - boxSize) / 2
        val right = left + boxSize
        val bottom = top + boxSize

        // ২. বাইরের অন্ধকার অংশ (ধূসর আভা)
        drawRect(
            color = Color.Black.copy(alpha = 0.6f)
        )

        // ৩. মাঝখানের অংশটি 'ফাঁকা' বা স্বচ্ছ করা (Punch out)
        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(boxSize, boxSize),
            cornerRadius = CornerRadius(20.dp.toPx()),
            blendMode = BlendMode.Clear
        )

        // ৪. স্ক্যানার বক্সের বর্ডার বা কর্নার আঁকা
        val strokeWidth = 4.dp.toPx()
        val cornerLength = 40.dp.toPx()
        val brandColor = Color(0xFF1B5E20) // আপনার BrandGreen

        // চার কোণায় ছোট বর্ডার (L Shape)
        // Top-Left
        drawLine(brandColor, Offset(left, top + cornerLength), Offset(left, top), strokeWidth)
        drawLine(brandColor, Offset(left, top), Offset(left + cornerLength, top), strokeWidth)

        // Top-Right
        drawLine(brandColor, Offset(right - cornerLength, top), Offset(right, top), strokeWidth)
        drawLine(brandColor, Offset(right, top), Offset(right, top + cornerLength), strokeWidth)

        // Bottom-Left
        drawLine(brandColor, Offset(left, bottom - cornerLength), Offset(left, bottom), strokeWidth)
        drawLine(brandColor, Offset(left, bottom), Offset(left + cornerLength, bottom), strokeWidth)

        // Bottom-Right
        drawLine(brandColor, Offset(right - cornerLength, bottom), Offset(right, bottom), strokeWidth)
        drawLine(brandColor, Offset(right, bottom), Offset(right, bottom - cornerLength), strokeWidth)

        // ৫. চলন্ত স্ক্যানিং লাইন (Moving Line)
        val lineY = top + (boxSize * lineOffset)
        drawLine(
            color = brandColor,
            start = Offset(left + 10.dp.toPx(), lineY),
            end = Offset(right - 10.dp.toPx(), lineY),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun CameraPreviewView(onResult: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val executor = ContextCompat.getMainExecutor(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // ১. প্রিভিউ কনফিগারেশন
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // ২. ব্যাক ক্যামেরা সিলেক্টর
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // ৩. ইমেজ অ্যানালাইসিস (QR স্ক্যানিং এর জন্য)
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                try {
                    // আগের সব বাইন্ডিং রিলিজ করা
                    cameraProvider.unbindAll()

                    // লাইফসাইকেলের সাথে ক্যামেরা বাইন্ড করা
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, executor)

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}
// --- কম্পোনেন্ট মডিউলসমূহ ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(onMenuClick: () -> Unit, onLogout: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrandGreen),
        title = { Text("Trust Money", color = White, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = White)
            }
        },
        actions = {
            IconButton(onClick = { }) { Icon(Icons.Default.Search, null, tint = White) }
            IconButton(onClick = { }) { Icon(Icons.Default.NotificationsNone, null, tint = White) }
            IconButton(onClick = onLogout) { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = White) }
        }
    )
}

@Composable
fun ProfileHeaderSection(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandGreen)
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(30.dp), tint = BrandGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Welcome to Trust Money", color = White.copy(alpha = 0.8f), fontSize = 12.sp)
        Text(userName, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CategoryTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf("Local Card", "Duel Currency")
    val icons = listOf(Icons.Default.CreditCard, Icons.Default.CreditScore)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp) // কার্ড দুটির মাঝখানে গ্যাপ রাখার জন্য
    ) {
        items.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            Card(
                modifier = Modifier
                    .weight(1f) // এটি কার্ড দুটিকে সমানভাবে পুরো Width দখল করতে সাহায্য করবে
                    .height(70.dp) // উচ্চতা কমিয়ে দেওয়া হয়েছে যাতে ভার্টিক্যালি কম জায়গা নেয়
                    .clickable { onTabSelected(index) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) BrandGreen else Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color.White else BrandGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}
@Composable
fun TabContentArea(selectedTab: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF004D40))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Column {
                Text("Trust Bank PLC.", color = White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Text("MD HABIBUR RAHMAN", color = White, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(if (selectedTab == 1) "Credit Card" else "Savings Account", color = White.copy(0.7f))
                Text("See Balance", color = White, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.Share, null, tint = White, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun DashboardBottomBar(    onHomeClick: () -> Unit,   onLocationClick: () -> Unit) {


    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 10.dp,
        modifier = Modifier.height(70.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ১. হোম বাটন
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onHomeClick() }
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint =  BrandGreen
                )
                Text("Home", color =  BrandGreen , fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // ৩. লোকেশন বাটন
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onLocationClick() }
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint =BrandGreen
                )
                Text("Location", color = BrandGreen , fontSize = 12.sp)
            }
        }
    }
}

// ৪. ডামি স্ক্রিন কম্পোনেন্ট


@Composable
fun DashboardDrawerContent() {
    // স্ক্রিনশটের মতো ড্রয়ারের প্রস্থ (Width) সীমিত করা হয়েছে
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp), // এখানে প্রস্থ কমিয়ে দেওয়া হয়েছে
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(0.dp) // ড্রয়ার সাধারণত সোজা থাকে
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ১. ড্রয়ার হেডার (সবুজ অংশ - স্ক্রিনশট অনুযায়ী)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandGreen)
                    .statusBarsPadding()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "MD HABIBUR RAHMAN",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Last login on April 02, 2026 at 01:05 pm",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }

            // ২. ড্রয়ার মেনু আইটেমসমূহ (স্ক্রিনশটের লিস্ট অনুযায়ী)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // মেনু বড় হলে স্ক্রল করা যাবে
                    .padding(10.dp)
            ) {
                DrawerItem("About Us", Icons.Default.Info)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("Personal info", Icons.Default.AccountCircle)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("Password Change", Icons.Default.LockReset)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("Disable Biometric", Icons.Default.Fingerprint)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("How to Use", Icons.Default.ChatBubbleOutline)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("Security Question Change", Icons.Default.HelpOutline)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("Sync Account", Icons.Default.PersonAddAlt)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                DrawerItem("Website", Icons.Default.Web)
            }
        }
    }
}

@Composable

fun DrawerItem(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* ক্লিক লজিক */ }
            .padding(horizontal = 15.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandGreen,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}



@Composable
fun QuickActionsMenu(navController: NavController) {
    val menuItems = listOf(
        MenuItemData("Fund Transfer", Icons.Default.SwapHoriz, Screen.FundTransferMethodSelectionScreen.route),
        MenuItemData("Add Money", Icons.Default.AddCard, Screen.AddMoneyMethodSelectionScreen.route),
        MenuItemData("Mobile Recharge", Icons.Default.PhonelinkRing, Screen.MobileRechargeMethodSelectionScreen.route),
        MenuItemData("Credit Card Bill Pay", Icons.Default.CreditCard, Screen.CreditCardSelectionBillPayScreen.route),
        MenuItemData("Account Services", Icons.Default.ManageAccounts, Screen.AccountServiceSelectionScreen.route),
        MenuItemData("Card Services", Icons.Default.CreditScore, Screen.CardServiceSelectionScreen.route),
        MenuItemData("Bills & Fees Payment", Icons.Default.ReceiptLong, Screen.BillAndFeesPayerOrgSelectionScreen.route),
        MenuItemData("Beneficiary Manage", Icons.Default.GroupAdd, Screen.BeneficiaryManageSelectionScreen.route),
        MenuItemData("EMI & Offer Partners", Icons.Default.Percent, "route"),
        MenuItemData("Product", Icons.Default.Inventory2,"route"),
        MenuItemData("News & Events", Icons.Default.Newspaper, "route"),
        MenuItemData("Others",   Icons.Default.GridView, "route"),
        MenuItemData("History", Icons.Default.History, Screen.TransactionHistoryScreen.route),
        MenuItemData("Help", Icons.Default.HelpOutline, "route"),
        MenuItemData("Contact", Icons.Default.HeadsetMic, "route" )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .background(Color.White)
            .padding(top = 12.dp)
    ) {
        // ড্র্যাগ হ্যান্ডেল (শীর্ষের ছোট দাগটি)

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(menuItems) { item ->
                QuickActionItem(item.title, item.icon,  item.route, navController)
            }

            // ব্যানার স্লাইডার - এটি গ্রিডের শেষ আইটেম হিসেবে থাকবে এবং ৪টি কলাম জুড়ে থাকবে
            item(span = { GridItemSpan(4) }) {
                Spacer(modifier = Modifier.height(16.dp))
                MenuBannerSlider()
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector, route: String, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(route) }
    ) {
        Box(
            modifier = Modifier
                .size(65.dp) // স্ক্রিনশট অনুযায়ী একটু বড় সাইজ
                .background(Color(0xFFF7FBF7), RoundedCornerShape(16.dp)) // হালকা আভা
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1B5E20), // গাঢ় সবুজ/BrandGreen
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color.Black.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuBannerSlider() {
    // ৩টি ইমেজের স্লাইডার (এখানে ৩টি স্লাইড থাকবে)
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
        ) { page ->
            // এখানে আপনার ইমেজগুলো বসবে
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE8F5E9)), // ইমেজের বদলে ডামি কালার
                    contentAlignment = Alignment.Center
                ) {
                    val banners = listOf(
                        R.drawable.menu_slider_image3,
                        R.drawable.menu_slider_image1,
                        R.drawable.menu_slider_image3
                    )
                    Image(
                        painter = painterResource(id = banners[page]), // page ইনডেক্স অনুযায়ী ইমেজ নিবে
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // স্লাইড ইন্ডিকেটর (নিচের ছোট ফোটাগুলো)
        Row {
            repeat(3) { index ->
                val color = if (pagerState.currentPage == index) Color(0xFF1B5E20) else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}



data class MenuItemData(val title: String, val icon: ImageVector,val route: String)
