package com.wnapp.trustmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wnapp.trustmoney.ui.auth.ForgotCredentialsScreen
import com.wnapp.trustmoney.ui.auth.LoginScreen
import com.wnapp.trustmoney.ui.auth.OtpVerifyScreen
import com.wnapp.trustmoney.ui.dashboard.DashboardScreen
import com.wnapp.trustmoney.ui.home.MainScreen
import com.wnapp.trustmoney.ui.more.AgentListScreen
import com.wnapp.trustmoney.ui.more.BusinessLoanScreen
import com.wnapp.trustmoney.ui.more.ProfileScreen
import com.wnapp.trustmoney.ui.more.UploadDocScreen
import com.wnapp.trustmoney.ui.transaction.TrackingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.ForgotPass.route) {
            ForgotCredentialsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController )
        }






        composable(Screen.SignUpOtp.route) {
            OtpVerifyScreen(onBack = { navController.popBackStack() })
        }

        // মেইন অ্যাপ
        composable(Screen.Home.route) {
           MainScreen(mainNavController = navController) // যেখানে Bottom Navigation থাকবে
        }

        // ৪. প্রোফাইল স্ক্রিন (এখন এটি মেইন নেভ-গ্রাফের অংশ)
        composable(Screen.Profile.route) {
            ProfileScreen(onBack = { navController.popBackStack() })
        }

        // ৫. ডকুমেন্ট আপলোড স্ক্রিন
        composable(Screen.UploadDoc.route) {
           UploadDocScreen(onBack = { navController.popBackStack() })
        }

        // ৬. ট্র্যাকিং ট্রানজ্যাকশন
        composable(Screen.Tracking.route) {
           TrackingScreen(onBack = { navController.popBackStack() })
        }

        // AppNavigation.kt এর NavHost এর ভেতরে যোগ করুন:

        composable(Screen.AgentList.route) {
            AgentListScreen()
        }

        composable(Screen.BusinessLoan.route) {
            BusinessLoanScreen(onBack = { navController.popBackStack() })
        }


    }
}