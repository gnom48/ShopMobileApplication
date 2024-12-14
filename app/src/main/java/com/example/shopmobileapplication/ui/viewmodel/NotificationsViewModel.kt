package com.example.shopmobileapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.Notification
import com.example.shopmobileapplication.data.notifications.NotificationsRepository
import com.google.gson.Gson
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.createChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class NotificationsViewModelFactory(private val notificationsRepository: NotificationsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(notificationsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class NotificationsViewModel(private val repository: NotificationsRepository): BaseViewModel() {
    private val _notifications = mutableStateOf<List<Notification>>(emptyList())
    val notifications by _notifications

    suspend fun attachToRealtimeByScope(scope: CoroutineScope, supabaseClient: io.github.jan.supabase.SupabaseClient = com.example.shopmobileapplication.data.network.SupabaseClient.client) {
        val ch = supabaseClient.realtime.createChannel(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()+UserViewModel.currentUser.id) { }

        ch.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = Notification.tableName
        }.onEach { action: PostgresAction ->
            when(action) {
                is PostgresAction.Delete -> {
                    try { Gson().fromJson<Notification>(action.oldRecord.toString(), Notification::class.java) } catch (e: Exception) { null }?.let { oldRecord ->
                        _notifications.value = _notifications.value.filter { i -> i.id != oldRecord.id }
                    }
                }
                is PostgresAction.Update -> {
                    try { Gson().fromJson<Notification>(action.record.toString(), Notification::class.java) } catch (e: Exception) { null }?.let { newNotification ->
                        _notifications.value = _notifications.value.map { i ->
                            if (i.id == newNotification.id) {
                                newNotification
                            } else {
                                i
                            }
                        }
                    }
                }
                is PostgresAction.Insert -> {
                    try { Gson().fromJson<Notification>(action.record.toString(), Notification::class.java) } catch (e: Exception) { null }?.let { newNotification ->
                        val ml = _notifications.value.toMutableList()
                        ml.add(newNotification)
                        _notifications.value = ml
                    }
                }
                is PostgresAction.Select -> { }
                else -> { }
            }
        }.launchIn(scope)

        supabaseClient.realtime.connect()
        ch.join()
    }

    fun updateNotification(notification: Notification) {
        viewModelScope.launch {
            withLoading {
                repository.updateNotification(notification).onSuccess {
                    _error.value = null
//                    val t = _notifications.value
//                    _notifications.value = emptyList()
                    _notifications.value =  _notifications.value.map { i ->
                        if (i.id == notification.id) {
                            notification
                        } else {
                            i
                        }
                    }
                }.onFailure {
                    _error.value = it
                }
            }
        }
    }

    fun getNotification() {
        viewModelScope.launch {
            withLoading {
                repository.getAllNotificationByUserId(UserViewModel.currentUser.id).onSuccess {
                    _notifications.value = it
                    _error.value = null
                }.onFailure {
                    _notifications.value = emptyList()
                    _error.value = it
                }
            }
        }
    }

    fun closeConnection(supabaseClient: io.github.jan.supabase.SupabaseClient = com.example.shopmobileapplication.data.network.SupabaseClient.client) {
        viewModelScope.launch {
            withLoading {
                supabaseClient.realtime.removeAllChannels()
                supabaseClient.realtime.disconnect()
            }
        }
    }
}