package com.wnapp.trustmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.Coil
import com.wnapp.trustmoney.ui.theme.NECMoneyTheme
import com.wnapp.trustmoney.ui.navigation.AppNavigation
import com.wnapp.trustmoney.util.getSvgImageLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
       // splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }
        enableEdgeToEdge()
        // পুরো অ্যাপের জন্য ডিফল্ট ইমেজ লোডার সেট করা
        val imageLoader = getSvgImageLoader(this)
        Coil.setImageLoader(imageLoader)

        setContent {
            NECMoneyTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NECMoneyTheme {
        AppNavigation()
    }
}