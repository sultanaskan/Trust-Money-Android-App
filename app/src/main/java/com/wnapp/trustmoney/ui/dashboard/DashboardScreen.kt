package com.wnapp.trustmoney.ui.dashboard // আপনার প্রজেক্টের প্যাকেজ অনুযায়ী এটি পরিবর্তন করুন

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// আপনার ব্র্যান্ড কালারসমূহ (যদি আলাদা ফাইলে থাকে তবে সেখান থেকে ইম্পোর্ট করুন)
val TBL_Green_Dark = Color(0xFF00695C)
val White = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(1) } // Default Cards selected

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DashboardDrawerContent() }
    ) {
        Scaffold(
            topBar = {
                DashboardTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onLogout = { navController.navigate("login") }
                )
            },
            bottomBar = {
                DashboardBottomBar(
                    onQrClick = { /* ক্যামেরা ওপেন লজিক */ },
                    onLocationClick = { /* ম্যাপ ওপেন লজিক */ }
                )
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
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowUp,
                        contentDescription = "Open Menu",
                        tint = TBL_Green_Dark,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = White
                ) {
                    QuickActionsMenu()
                }
            }
        }
    }
}

// --- কম্পোনেন্ট মডিউলসমূহ ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(onMenuClick: () -> Unit, onLogout: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TBL_Green_Dark),
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
            .background(TBL_Green_Dark)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(90.dp).clip(CircleShape).background(White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(60.dp), tint = TBL_Green_Dark)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Welcome to Trust Money", color = White.copy(alpha = 0.8f), fontSize = 14.sp)
        Text(userName, color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CategoryTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf("Accounts", "Cards", "Loans", "Schemes")
    val icons = listOf(Icons.Default.AccountBalance, Icons.Default.CreditCard, Icons.Default.LocalHospital, Icons.Default.Eco)

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            Card(
                modifier = Modifier.size(80.dp).clickable { onTabSelected(index) },
                colors = CardDefaults.cardColors(containerColor = if (isSelected) TBL_Green_Dark else White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icons[index], null, tint = if (isSelected) White else TBL_Green_Dark)
                    Text(title, fontSize = 10.sp, color = if (isSelected) White else Color.Gray)
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
fun DashboardBottomBar(onQrClick: () -> Unit, onLocationClick: () -> Unit) {
    BottomAppBar(containerColor = White, tonalElevation = 10.dp) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Home, null, tint = TBL_Green_Dark)
                Text("Home", color = TBL_Green_Dark, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier.offset(y = (-20).dp).size(65.dp).clip(CircleShape).background(TBL_Green_Dark).clickable { onQrClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.QrCodeScanner, null, tint = White, modifier = Modifier.size(30.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onLocationClick() }) {
                Icon(Icons.Default.LocationOn, null, tint = Color.Gray)
                Text("Location", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DashboardDrawerContent() {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Main Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TBL_Green_Dark)
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            DrawerItem("Profile", Icons.Default.Person)
            DrawerItem("Settings", Icons.Default.Settings)
        }
    }
}

@Composable
fun DrawerItem(title: String, icon: ImageVector) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { }, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TBL_Green_Dark)
        Spacer(modifier = Modifier.width(15.dp))
        Text(title)
    }
}

@Composable
fun QuickActionsMenu() {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            QuickActionItem("Topup", Icons.Default.PhonelinkRing)
            QuickActionItem("Bill Pay", Icons.Default.ReceiptLong)
            QuickActionItem("Transfer", Icons.Default.SyncAlt)
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(50.dp).background(Color(0xFFF1F8F1), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = TBL_Green_Dark)
        }
        Text(title, fontSize = 12.sp)
    }
}