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
    object FundTransfer: Screen("fund_transfer")
    object BillAndFeesPayerOrgSelectionScreen: Screen("bill_fees_payer_org_selection_screen")
    object CreditCardSelectionBillPayScreen: Screen("credit_card_selection_bill_pay_screen")
    object FundTransferMethodSelectionScreen: Screen("fundTransferMethodSelectionScreen")
    object MobileRechargeMethodSelectionScreen: Screen("mobilerechargemethodselectionscreen")
    object AccountServiceSelectionScreen: Screen("account_services_selection_screen")
    object BeneficiaryManageSelectionScreen: Screen("beneficiary_manage_selection_screen")
    object CardServiceSelectionScreen: Screen("CardServicesSelectionScreen")
    object TransactionHistoryScreen: Screen("TransactionHistorySelectionScreen")
    object PackageScreen: Screen("package_screen")
    object MethodSelection: Screen("add_money_method_selection_screen/{amount}/{transactionType}"){
        fun passAmountAndTransactionType(amount: String, transactionType: String): String{
            return "add_money_method_selection_screen/$amount/$transactionType"
        }
    }
    object AddBalance: Screen("add_balance/{amount}"){
        fun passAmount(amount: String): String{
            return "add_balance/$amount"
        }
    }
    object MoneyRequest: Screen("money_request/{amount}/{payment_type}"){
        fun passAmountAndMethod( amount: String, payment_type:String): String{
            return "money_request/$amount/$payment_type"
        }
    }
    object PaymentSubmitMobile: Screen("payment_submit_mobile/{amount}/{paymentMethodId}"){
        fun passAmountAndProvider(amount: String, paymentMethodId: String): String{
            return "payment_submit_mobile/$amount/$paymentMethodId"
        }
    }
    object PaymentSubmitBank: Screen("payment_submit_bank/{amount}/{paymentMethodId}"){
        fun passAmountAndProvider(amount: String, paymentMethodId: String): String{
            return "payment_submit_bank/$amount/$paymentMethodId"
        }
    }

    object Notification: Screen("notification")
    object AboutUs: Screen("about_us")
    object PinEnter: Screen("pin_enter")
    object ProfileSetting: Screen("profile_setting_screen")
    object Support: Screen("support_screen")
    object CompanyDocs: Screen("company_docs_screen")
    object InsufficientBalance: Screen("insufficient_balance_screen")
    object RequestHistory: Screen("request_history_screen")
    object SendMoney: Screen("send_money_screen/{amount}"){
        fun passAmount(amount: String): String{
            return "send_money_screen/$amount"
        }
    }


    object SendMoneyToMobile : Screen("send_money_to_mobile_screen/{amount}/{paymentMethodId}") {
        fun passAmountAndMethod(amount: String, paymentMethodId: String): String {
            return "send_money_to_mobile_screen/$amount/$paymentMethodId"
        }
    }
    object SendMoneyToBank : Screen("send_money_to_bank_screen/{amount}/{paymentMethodId}") {
        fun passAmountAndMethod(amount: String, paymentMethodId: String): String {
            return "send_money_to_bank_screen/$amount/$paymentMethodId"
        }
    }

    object UploadDocument: Screen("upload_document_screen")

    object MobileRecharge : Screen("mobile_recharge?amount={amount}") {
        fun passAmount(amount: String = "0"): String {
            return "mobile_recharge?amount=$amount"
        }
    }

    object MobileRechargeConfirmation : Screen("recharge_confirm/{amount}/{paymentMethodId}") {
        fun passAmountAndMethod(amount: String, paymentMethodId: String): String {
            return "recharge_confirm/$amount/$paymentMethodId"
        }
    }


}