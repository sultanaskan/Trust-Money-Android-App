package com.wnapp.trustmoney.data.remote

import com.wnapp.trustmoney.data.model.BannerResponse
import com.wnapp.trustmoney.data.model.CompanyDocModel
import com.wnapp.trustmoney.data.model.LoginCreds
import com.wnapp.trustmoney.data.model.LoginResponse
import com.wnapp.trustmoney.data.model.RegistrationFormData
import com.wnapp.trustmoney.data.model.RegistrationResponse
import com.wnapp.trustmoney.data.model.CurrencyItem
import com.wnapp.trustmoney.data.model.DeleteResponse
import com.wnapp.trustmoney.data.model.GetMoneyRequestResponse
import com.wnapp.trustmoney.data.model.HistoryResponse
import com.wnapp.trustmoney.data.model.MoneyRequestResponse
import com.wnapp.trustmoney.data.model.NotificationResponse
import com.wnapp.trustmoney.data.model.Package
import com.wnapp.trustmoney.data.model.PaymentMethodItem
import com.wnapp.trustmoney.data.model.PaymentMethodResponse
import com.wnapp.trustmoney.data.model.SinglePaymentMethodResponse
import com.wnapp.trustmoney.data.model.StatusResponse
import com.wnapp.trustmoney.data.model.TransactionModel
import com.wnapp.trustmoney.data.model.VerificationResponse
import com.wnapp.trustmoney.data.model.WalletModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("user/register")
    suspend fun registerUser(@Body request: RegistrationFormData): Response<RegistrationResponse>

    @GET("currency")
    suspend fun getCurrencies(): List<CurrencyItem>

    // নির্দিষ্ট একটি কারেন্সি রেট পাওয়ার জন্য
    @GET("currency/{id}")
    suspend fun getCurrency(@Path("id") currencyId: Int): CurrencyItem
    @GET("package")
    suspend fun gePackages(): List<Package>

    @GET("payment")
    suspend fun getPaymentMethods(): PaymentMethodResponse

    // API Interface
    @GET("payment/{id}")
    suspend fun getPaymentMethod(@Path("id") id: Int): SinglePaymentMethodResponse

    @GET("roles") // আপনার আসল এন্ডপয়েন্ট নাম দিন
    suspend fun getRoles(): List<String>

    @POST("user/login")
    suspend fun loginUser(@Body request: LoginCreds): Response<LoginResponse>


    @Multipart
    @POST("money_request")
    suspend fun createMoneyRequest(
        @Part("userId") userId: RequestBody,
        @Part("paymentMethod") paymentMethod: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("type") type: RequestBody,
        @Part("transactionId") transactionId: RequestBody?,
        @Part recitImage: MultipartBody.Part?
    ): Response<MoneyRequestResponse>


    @GET("notification/user/{userId}")
    suspend fun getUserNotifications(
        @Path("userId") userId: Int
    ): Response<NotificationResponse>

    @PUT("notification/read/{id}")
    suspend fun markAsRead(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("wallet/{userId}")
    suspend fun getWallet(
        @Path("userId") userId: Int
    ): Response<WalletModel>

    @GET("/api/transactions/user/{userId}")
    suspend fun getUserTransactions(
        @Path("userId") userId: Int
    ): Response<List<TransactionModel>>

    @GET("banner")
    suspend fun getBanners(): Response<BannerResponse>

    @GET("doc")
    suspend fun getAllDocs(): Response<List<CompanyDocModel>>


    // File: ApiService.kt
    @GET("money_request/my/{userId}")
    suspend fun getMyRequests(
        @Path("userId") userId: Int
    ): Response<GetMoneyRequestResponse> // Changed from MoneyRequestResponse to GetMoneyRequestResponse

    @Multipart
    @POST("verification/upload")
    suspend fun uploadVerification(
        @Part("userId") userId: RequestBody,
        @Part("docType") docType: RequestBody,
        @Part("docNumber") docNumber: RequestBody,
        @Part frontPartImage: MultipartBody.Part,
        @Part backPartImage: MultipartBody.Part? // Optional for some documents
    ): Response<VerificationResponse>

    @GET("verification/my-status/{userId}")
    suspend fun getMyStatus(
        @Path("userId") userId: Int
    ): Response<StatusResponse>

    @GET("verification/my-status/{userId}")
    suspend fun getVerificationHistory(
        @Path("userId") userId: Int
    ): Response<HistoryResponse>


    @DELETE("verification/admin/delete/{id}")
    suspend fun deleteVerification(
        @Path("id") id: Int
    ): Response<DeleteResponse>



}