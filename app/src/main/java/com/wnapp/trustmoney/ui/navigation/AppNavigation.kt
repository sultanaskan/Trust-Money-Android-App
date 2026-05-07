package com.wnapp.trustmoney.ui.navigation

import CreditCardSelectionBillPayScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wnapp.trustmoney.data.local.SessionManager
import com.wnapp.trustmoney.data.repository.TransactionRepository
import com.wnapp.trustmoney.ui.auth.AuthScreen
import com.wnapp.trustmoney.ui.auth.ForgotCredentialsScreen
import com.wnapp.trustmoney.ui.auth.OtpVerifyScreen
import com.wnapp.trustmoney.ui.more.AboutUsScreen
import com.wnapp.trustmoney.ui.more.AgentListScreen
import com.wnapp.trustmoney.ui.more.BusinessLoanScreen
import com.wnapp.trustmoney.ui.transaction.PackageScreen
import com.wnapp.trustmoney.ui.more.ProfileScreen
import com.wnapp.trustmoney.ui.AddMoneyScreen
import com.wnapp.trustmoney.ui.DocumentUploadScreen
import com.wnapp.trustmoney.ui.MoneyRequestScreen
import com.wnapp.trustmoney.ui.transaction.TransactionHistoryScreen
import com.wnapp.trustmoney.ui.MethodSelectionScreen
import com.wnapp.trustmoney.ui.InsufficientBalanceScreen
import com.wnapp.trustmoney.ui.ProfileSettingScreen
import com.wnapp.trustmoney.ui.SendMoneyScreen
import com.wnapp.trustmoney.ui.SupportScreen
import com.wnapp.trustmoney.ui.auth.PinEntryScreen
import com.wnapp.trustmoney.ui.home.HomeScreen
import com.wnapp.trustmoney.ui.more.CompanyDocsScreen
import com.wnapp.trustmoney.ui.transaction.NotificationScreen
import com.wnapp.trustmoney.ui.transaction.DepositWithBankScreen
import com.wnapp.trustmoney.ui.transaction.DepositWithMobileScreen
import com.wnapp.trustmoney.ui.transaction.MobileRechargeConfirmationScreen
import com.wnapp.trustmoney.ui.transaction.MobileRechargeScreen
import com.wnapp.trustmoney.ui.transaction.RequestHistoryScreen
import com.wnapp.trustmoney.ui.transaction.SendMoneyToBankScreen
import com.wnapp.trustmoney.ui.transaction.SendMoneyToMobileScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.BillAndFeesPayerOrgSelectionScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.FundTransferMethodSelectionScreen
import com.wnapp.trustmoney.ui.transaction.methodselection.MobileRechargeMethodSelectionScreen
import com.wnapp.trustmoney.ui.transaction.servicesandmanage.AccountServiceSelectionScreen
import com.wnapp.trustmoney.ui.transaction.servicesandmanage.BeneficiaryManageSelectionScreen
import com.wnapp.trustmoney.ui.transaction.servicesandmanage.CardServiceSelectionScreen
import com.wnapp.trustmoney.viewmodel.NotificationViewModel
import com.wnapp.trustmoney.viewmodel.NotificationViewModelFactory

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
        composable(Screen.PackageScreen.route){
            PackageScreen( navController = navController)
        }
        composable(
            route = Screen.AddBalance.route,
            arguments = listOf(navArgument("amount") { type = NavType.StringType })
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            AddMoneyScreen(navController = navController, initialAmount = amount)
        }
        composable(route = Screen.MoneyRequest.route,arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("payment_type") { type = NavType.StringType } // ২য় আর্গুমেন্ট যোগ করা হলো
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            val paymentType = backStackEntry.arguments?.getString("payment_type") ?: "mobile"
            MoneyRequestScreen(
                navController = navController,
                amount = amount,
                paymentType = paymentType
            )
        }
        composable(route = Screen.MethodSelection.route,
            arguments = listOf(
                navArgument("amount"){type = NavType.StringType},
                navArgument("transactionType"){type = NavType.StringType}
            )
        ){ backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val transactionType = backStackEntry.arguments?.getString("transactionType") ?: ""
            MethodSelectionScreen(navController = navController,amount,transactionType )
        }


        composable(
            // রাউটে অবশ্যই ভেরিয়েবলগুলো ডিফাইন থাকতে হবে
            route = Screen.PaymentSubmitMobile.route,
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("paymentMethodId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val paymentMethodId = backStackEntry.arguments?.getString("paymentMethodId") ?: ""

            DepositWithMobileScreen(
                navController = navController,
                amount = amount,
                paymentMethodId = paymentMethodId
            )
        }
        composable(
            route = Screen.PaymentSubmitBank.route,
            arguments = listOf(
                navArgument("amount"){type = NavType.StringType},
                navArgument("paymentMethodId"){type = NavType.StringType}
            )
        )
        { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val paymentMethodId = backStackEntry.arguments?.getString("paymentMethodId") ?: ""
            DepositWithBankScreen(navController = navController,amount = amount, paymentMethodId = paymentMethodId  )
        }


        composable(Screen.Notification.route) {
            val context = LocalContext.current

            // ১. রিপোজিটরি ইন্সট্যান্স তৈরি
            val repository = remember { TransactionRepository(context) }

            // ২. ফ্যাক্টরি ব্যবহার করে ভিউমডেল তৈরি
            val notificationViewModel: NotificationViewModel = viewModel(
                factory = NotificationViewModelFactory(repository)
            )

            val sm = remember { SessionManager(context) }
            val currentUserId = sm.getUserId() ?: 0

            NotificationScreen(navController)
        }


        composable(Screen.AboutUs.route){
            AboutUsScreen(navController)
        }
        composable(Screen.PinEnter.route){
            PinEntryScreen( navController)
        }
        composable(Screen.ProfileSetting.route){
            ProfileSettingScreen(navController)
        }
        composable(Screen.Support.route){
            SupportScreen(navController)
        }
        composable(Screen.CompanyDocs.route){
            CompanyDocsScreen(navController)
        }
        composable(Screen.InsufficientBalance.route){
            InsufficientBalanceScreen(navController)
        }

        composable(Screen.RequestHistory.route){
            RequestHistoryScreen(navController)
        }


        // ১. Send Money Screen Composable
        composable(
            route = Screen.SendMoney.route,
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount")
            SendMoneyScreen(
                navController = navController,
                initialAmount = amount
            )
        }

        // ২. Mobile Recharge Screen Composable
        composable(
            route = Screen.MobileRecharge.route,
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount")

            // এখানে আপনার MobileRechargeScreen কম্পোজেবলটি কল করবেন
            // উদাহরণ হিসেবে:
            /*
            MobileRechargeScreen(
                navController = navController,
                amount = amount
            )
            */
        }

        composable(
            route = Screen.SendMoneyToMobile.route,
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    defaultValue = "0"
                },
                navArgument("paymentMethodId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            // আর্গুমেন্ট থেকে ভ্যালুগুলো গেট করা
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            val paymentMethodId = backStackEntry.arguments?.getString("paymentMethodId") ?: ""

            // SendMoneyToMobileScreen-এ ডেটা পাস করা
            SendMoneyToMobileScreen(
                navController = navController,
                amount = amount,
                paymentMethodId = paymentMethodId
            )
        }
        composable(
            route = Screen.SendMoneyToBank.route,
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    defaultValue = "0"
                },
                navArgument("paymentMethodId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            // আর্গুমেন্ট থেকে ভ্যালুগুলো গেট করা
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            val paymentMethodId = backStackEntry.arguments?.getString("paymentMethodId") ?: ""

            // SendMoneyToMobileScreen-এ ডেটা পাস করা
            SendMoneyToBankScreen(
                navController = navController,
                amount = amount,
                paymentMethodId = paymentMethodId
            )
        }


        composable(Screen.UploadDocument.route){
            DocumentUploadScreen(navController)
        }


        composable(
            route = Screen.MobileRecharge.route,
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount")
            MobileRechargeScreen(
                navController = navController,
                initialAmount = amount
            )
        }




        composable(
            route = Screen.MobileRechargeConfirmation.route,
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("paymentMethodId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            val paymentMethodId = backStackEntry.arguments?.getString("paymentMethodId") ?: "0"

            MobileRechargeConfirmationScreen(
                navController = navController,
                amount = amount,
                paymentMethodId = paymentMethodId
            )
        }

















        composable(Screen.SignUpOtp.route) {
            OtpVerifyScreen(onBack = { navController.popBackStack() })
        }

        // মেইন অ্যাপ
        composable(Screen.Home.route) {
           HomeScreen(navController = navController) // যেখানে Bottom Navigation থাকবে
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