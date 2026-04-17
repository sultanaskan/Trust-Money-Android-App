package com.wnapp.trustmoney.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object SignUp1 : Screen("signup_screen1")
    object SignUpOtp: Screen("sign_up_otp")
    object Home : Screen("home_screen")
    object History : Screen("history_screen")
    object Gift: Screen("gift_screen");
    object More: Screen("more_screen")
    object Profile : Screen("profile_screen")
    object Tracking : Screen("tracking_screen")
    object UploadDoc : Screen("upload_doc_Screen")
    object AgentList : Screen("agent_list") // বানান চেক করুন
    object BusinessLoan : Screen("business_loan")
    object ForgotPass : Screen("forgot_pass")
    object Dashboard: Screen("dashboard")


}