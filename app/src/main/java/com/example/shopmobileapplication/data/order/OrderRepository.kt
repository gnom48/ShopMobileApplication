package com.example.shopmobileapplication.data.order

import com.example.shopmobileapplication.data.Order
import com.example.shopmobileapplication.data.OrderDetailsView
import com.example.shopmobileapplication.data.User

interface OrderRepository {
    suspend fun addOrder(orders: List<Order>): Result<String>
    suspend fun getOrdersDetails(user: User, orderId: String? = null): Result<List<OrderDetailsView>>
}