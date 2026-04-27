package com.wnapp.trustmoney.ui.navigation

import CreditCardSelectionBillPayScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wnapp.trustmoney.ui.auth.AuthScreen
import com.wnapp.trustmoney.ui.auth.ForgotCredentialsScreen
import com.wnapp.trustmoney.ui.auth.OtpVerifyScreen
import com.wnapp.trustmoney.ui.dashboard.DashboardScreen
import com.wnapp.trustmoney.ui.home.MainScreen
import com.wnapp.trustmoney.ui.more.AgentListScreen
import com.wnapp.trustmoney.ui.more.BusinessLoanScreen
import com.wnapp.trustmoney.ui.more.ProfileScreen
import com.wnapp.trustmoney.ui.more.UploadDocScreen
import com.wnapp.trustmoney.ui.transaction.TransactionHistoryScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.AddMoneyMethodSelectionScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.BillAndFeesPayerOrgSelectionScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.FundTransferMethodSelectionScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.MobileRechargeMethodSelectionScreen
import com.wnapp.trustmoney.ui.transaction.servicesandmanage.AccountServiceSelectionScreen
import com.wnapp.trustmoney.ui.transaction.servicesandmanage.BeneficiaryManageSelectionScreen
import com.wnapp.trustmoney.ui.transaction.servicesandmanage.CardServiceSelectionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(navController = navController)
        }


        composable(Screen.ForgotPass.route) {
            ForgotCredentialsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController )
        }

        composable(Screen.AddMoneyMethodSelectionScreen.route){
            AddMoneyMethodSelectionScreen(navController = navController)
        }
        composable(Screen.BillAndFeesPayerOrgSelectionScreen.route){
            BillAndFeesPayerOrgSelectionScreen( navController = navController)
        }
        composable(Screen.CreditCardSelectionBillPayScreen.route){
            CreditCardSelectionBillPayScreen( navController = navController)
        }
        composable(Screen.FundTransferMethodSelectionScreen.route){
            FundTransferMethodSelectionScreen( navController = navController)
        }
        composable(Screen.MobileRechargeMethodSelectionScreen.route){
            MobileRechargeMethodSelectionScreen( navController = navController)
        }
        composable(Screen.AccountServiceSelectionScreen.route){
            AccountServiceSelectionScreen( navController = navController)
        }
        composable(Screen.BeneficiaryManageSelectionScreen.route){
            BeneficiaryManageSelectionScreen( navController = navController)
        }
        composable(Screen.CardServiceSelectionScreen.route){
            CardServiceSelectionScreen( navController = navController)
        }
        composable(Screen.TransactionHistoryScreen.route){
            TransactionHistoryScreen( navController = navController)
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


        // AppNavigation.kt এর NavHost এর ভেতরে যোগ করুন:

        composable(Screen.AgentList.route) {
            AgentListScreen()
        }

        composable(Screen.BusinessLoan.route) {
            BusinessLoanScreen(onBack = { navController.popBackStack() })
        }


    }
}