import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardSelectionBillPayScreen(navController: NavController) {
    val brandGreen = Color(0xFF004D40) // ট্রাস্ট মানি ডার্ক গ্রিন

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Credit Card Bill Pay",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = brandGreen
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(16.dp)
        ) {
            // ১. ইনস্ট্রাকশন কার্ড (Select your Credit Card Bill option)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FBF8))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Select your ")
                        withStyle(style = SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                            append("Credit Card")
                        }
                        append(" Bill option")
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ২. বিল পে অপশন লিস্ট
            BillOptionItem(
                title = "TBL Own Credit Card Bill Pay",
                icon = Icons.Default.CreditCard,
                brandColor = brandGreen
            ) {
                // অ্যাকশন
            }

            Spacer(modifier = Modifier.height(12.dp))

            BillOptionItem(
                title = "TBL Others Credit Card Bill Pay",
                icon = Icons.Default.CreditCard,
                brandColor = brandGreen
            ) {
                // অ্যাকশন
            }

            Spacer(modifier = Modifier.height(12.dp))

            BillOptionItem(
                title = "Other Bank Credit Card Bill Pay",
                icon = Icons.Default.CreditCard,
                brandColor = brandGreen
            ) {
                // অ্যাকশন
            }

            // ব্যাকগ্রাউন্ড ওয়াটারমার্কের জন্য স্পেস
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun BillOptionItem(
    title: String,
    icon: ImageVector,
    brandColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(1.dp, brandColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // কার্ড আইকন (স্ক্রিনশট অনুযায়ী ছোট গ্রিন কার্ড আইকন)
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF1F8F1), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = brandColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // অপশন টাইটেল
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = brandColor
        )

        // ডান পাশের অ্যারো
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = brandColor,
            modifier = Modifier.size(20.dp)
        )
    }
}