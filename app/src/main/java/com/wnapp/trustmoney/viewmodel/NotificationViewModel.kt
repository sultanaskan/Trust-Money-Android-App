package com.wnapp.trustmoney.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wnapp.trustmoney.data.model.NotificationModel
import com.wnapp.trustmoney.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: TransactionRepository) : ViewModel() {

    var notifications = mutableStateListOf<NotificationModel>()
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    fun fetchNotifications(userId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getNotifications(userId)
                if (response.isSuccessful && response.body()?.success == true) {
                    notifications.clear()
                    notifications.addAll(response.body()!!.data)
                } else {
                    errorMessage.value = "Failed to load data"
                }
            } catch (e: Exception) {
                errorMessage.value = e.localizedMessage ?: "Unknown Error"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markRead(notificationId)
            // লোকাল লিস্ট আপডেট করা যাতে সাথে সাথে UI পরিবর্তন হয়
            val index = notifications.indexOfFirst { it.id == notificationId }
            if (index != -1) {
                notifications[index] = notifications[index].copy(isRead = true)
            }
        }
    }

}




class NotificationViewModelFactory(private val repository: TransactionRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



