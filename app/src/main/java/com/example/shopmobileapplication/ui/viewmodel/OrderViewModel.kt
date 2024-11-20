package com.example.shopmobileapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopmobileapplication.data.Order
import com.example.shopmobileapplication.data.OrderDetailsView
import com.example.shopmobileapplication.data.User
import com.example.shopmobileapplication.data.order.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModelFactory(private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class OrderViewModel(
    private val orderRepository: OrderRepository
): BaseViewModel() {
    private val _orders = mutableStateOf<List<Order>>(emptyList())
    val orders by _orders

    private val _newOrderId = mutableStateOf<String?>(null)
    val newOrderId by _newOrderId

    fun dismissSuccess() {
        _newOrderId.value = null
    }

    suspend fun addOrder(ordersList: List<Order>) {
        viewModelScope.launch {
            withLoading {
                orderRepository.addOrder(ordersList).onSuccess {
                    _orders.value = ordersList
                    _newOrderId.value = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                }
            }

        }
    }

    private val _orderDetails = mutableStateOf<List<OrderDetailsView>>(emptyList())
    val orderDetails by _orderDetails

    suspend fun getOrdersDetails(user: User, orderId: String? = null) {
        viewModelScope.launch {
            withLoading {
                orderRepository.getOrdersDetails(user, orderId).onSuccess {
                    _orderDetails.value = it
                    _error.value = null
                }.onFailure { e ->
                    _error.value = e
                    _orderDetails.value = emptyList()
                }
            }
        }
    }
}