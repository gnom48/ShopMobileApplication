package com.example.shopmobileapplication.data.order

import com.example.shopmobileapplication.data.Order
import com.example.shopmobileapplication.data.OrderDetailsView
import com.example.shopmobileapplication.data.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import java.util.UUID

class OrderRepositoryImpl(
    private val supabaseClient: SupabaseClient
): OrderRepository {
    override suspend fun addOrder(orders: List<Order>): Result<String> = try {
        val orderSingleId = UUID.randomUUID().toString()
        orders.forEach { order: Order ->
            order.id = orderSingleId
            supabaseClient.postgrest[Order.tableName].insert(order)
        }
        Result.success(orderSingleId)
    } catch (e: Exception) {
        Result.failure(e)
    }

//    override suspend fun addOrder(orders: List<Order>): Result<String> = try {
//        val orderSingleId = UUID.randomUUID().toString()
//
//        supabaseClient.postgrest.rpc("begin") // FIXME: не работает транзакция (а хотелось бы)
//        try {
//            orders.forEach { order: Order ->
//                order.id = orderSingleId
//                supabaseClient.postgrest[Order.tableName].insert(order)
//            }
//            supabaseClient.postgrest.rpc("commit")
//            Result.success(orderSingleId)
//        } catch (e: Exception) {
//            supabaseClient.postgrest.rpc("rollback")
//            Result.failure(e)
//        }
//    } catch (e: Exception) {
//        Result.failure(e)
//    }

    override suspend fun getOrdersDetails(user: User, orderId: String?): Result<List<OrderDetailsView>> = try {
        Result.success(supabaseClient.postgrest[OrderDetailsView.viewName].select(filter = {
            if (orderId != null) {
                and {
                    OrderDetailsView::orderId eq orderId
                    OrderDetailsView::userId eq user.id
                }
            } else {
                OrderDetailsView::userId eq user.id
            }
        }).decodeList<OrderDetailsView>())
    } catch (e: Exception) {
        Result.failure(e)
    }
}