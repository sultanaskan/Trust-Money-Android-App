package com.wnapp.trustmoney.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth_screen")
    object SignUpOtp: Screen("sign_up_otp")
    object Home : Screen("home_screen")
    object History : Screen("history_screen")
    object Gift: Screen("gift_screen");
    object More: Screen("more_screen")
    object Profile : Screen("profile_screen")
    object AgentList : Screen("agent_list") // বানান চেক করুন
    object BusinessLoan : Screen("business_loan")
    object ForgotPass : Screen("forgot_pass")
    object Dashboard: Screen("dashboard")
    object FundTransfer: Screen("fund_transfer")
    object AddMoneyMethodSelectionScreen: Screen("add_money_method_selection_screen")
    object BillAndFeesPayerOrgSelectionScreen: Screen("bill_fees_payer_org_selection_screen")
    object CreditCardSelectionBillPayScreen: Screen("credit_card_selection_bill_pay_screen")
    object FundTransferMethodSelectionScreen: Screen("fundTransferMethodSelectionScreen")
    object MobileRechargeMethodSelectionScreen: Screen("mobilerechargemethodselectionscreen")
    object AccountServiceSelectionScreen: Screen("account_services_selection_screen")
    object BeneficiaryManageSelectionScreen: Screen("beneficiary_manage_selection_screen")
    object CardServiceSelectionScreen: Screen("CardServicesSelectionScreen")
    object TransactionHistoryScreen: Screen("TransactionHistorySelectionScreen")


}