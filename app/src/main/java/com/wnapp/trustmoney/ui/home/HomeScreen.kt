package com.wnapp.trustmoney.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wnapp.trustmoney.ui.theme.BrandGreen
import com.wnapp.trustmoney.R

@Composable
fun HomeScreen() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // ১. প্রোফাইল এবং নোটিফিকেশন হেডার
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .background(BrandGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Hello,", color = BrandGreen, fontSize = 16.sp)
                        Text("Md Rasel Mollah", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                IconButton(onClick = { }, modifier = Modifier.background(BrandGreen.copy(alpha = 0.1f), CircleShape)) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = BrandGreen)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ২. রিসেন্ট ট্রানজ্যাকশন অ্যানাউন্সমেন্ট বার
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                color = Color.Gray.copy(alpha = 0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "+965 886 **** send Money 540.00৳ Bkash",
                        color = Color.White,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ৩. ইনভাইট এবং রিওয়ার্ড সেকশন
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                InviteChip(text = "Invite", icon = Icons.Default.PersonAdd)
                Spacer(modifier = Modifier.width(12.dp))
                Text("& Get", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(12.dp))
                InviteChip(text = "SAR 50.00", icon = Icons.Default.CardGiftcard, isAmount = true)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ৪. এক্সচেঞ্জ রেট এবং কনভার্টার সেকশন
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null, modifier = Modifier.size(30.dp).clip(CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bangladesh")
                    }
                    Text("1 SAR = 37.4 BDT", color = Color.Red, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ৫. কারেন্সি ইনপুট রো
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyInputBox(label = "SAR", value = "0.00", flagIcon = Icons.Default.Flag)
                IconButton(onClick = { }, modifier = Modifier.background(Color.White, CircleShape).size(45.dp)) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(30.dp))
                }
                CurrencyInputBox(label = "BDT", value = "0.00", flagIcon = Icons.Default.Flag)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Recipients get an extra 2.5%", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // ৬. কন্টিনিউ বাটন
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) {
                Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ৭. ইন্সট্যান্ট ডিপোজিট কার্ড
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(50.dp).background(BrandGreen, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AddBusiness, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Instant Capital Deposit", fontWeight = FontWeight.Bold, color = BrandGreen)
                        Text("ZERO FEE · INSTANT", fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                }
            }
        }
    }


@Composable
fun InviteChip(text: String, icon: ImageVector, isAmount: Boolean = false) {
    Surface(
        color = BrandGreen,
        shape = RoundedCornerShape(50.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CurrencyInputBox(label: String, value: String, flagIcon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(flagIcon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontWeight = FontWeight.Bold)
        }
        Text(value, fontSize = 24.sp, color = Color.LightGray)
        Box(modifier = Modifier.width(80.dp).height(1.dp).background(Color.LightGray))
    }
}
