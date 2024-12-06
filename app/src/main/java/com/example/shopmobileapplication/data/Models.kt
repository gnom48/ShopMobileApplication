package com.example.shopmobileapplication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

abstract class BaseModel { }

@Serializable
data class User(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("image") var image: String?,
    @SerialName("address") var address: String?
) : BaseModel() {
    companion object {
        const val tableName = "users"
    }
}

@Serializable
data class Product(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("category") var category: Int,
    @SerialName("description") var description: String,
    @SerialName("price") var price: Double,
    @SerialName("image") var image: String?,
    @SerialName("seller_id") var sellerId: Int
) : BaseModel() {
    companion object {
        const val tableName = "products"
    }
}

@Serializable
data class Bucket(
    @SerialName("user_id") var userId: String,
    @SerialName("product_example_id") var productExampleId: Int,
    @SerialName("quantity") var quantity: Int
) : BaseModel() {
    companion object {
        const val tableName = "buckets"
    }
}

@Serializable
data class Favorite(
    @SerialName("user_id") var userId: String,
    @SerialName("product_id") var productId: String
) : BaseModel() {
    companion object {
        const val tableName = "favorites"
    }
}

@Serializable
data class Seller(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    @SerialName("image") var image: String?
) : BaseModel() {
    companion object {
        const val tableName = "sellers"
    }
}

@Serializable
data class ProductCategory(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String
) : BaseModel() {
    companion object {
        const val tableName = "categories"
    }
}

@Serializable
data class Store(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    @SerialName("address") var address: String,
    @SerialName("lat") var lat: Double,
    @SerialName("lon") var lon: Double
) : BaseModel() {
    companion object {
        const val tableName = "stores"
    }
}

@Serializable
data class Stock(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    @SerialName("address") var address: String,
    @SerialName("lat") var lat: Double,
    @SerialName("lon") var lon: Double
) : BaseModel() {
    companion object {
        const val tableName = "stocks"
    }
}

@Serializable
data class Order(
    @SerialName("id") var id: String,
    @SerialName("user_id") var userId: String,
    @SerialName("product_example_id") var productExampleId: Int,
    @SerialName("quantity") var quantity: Int,
    @SerialName("order_date_time") var orderDateTime: Long,
    @SerialName("store_id") var storeId: Int
) : BaseModel() {
    companion object {
        const val tableName = "orders"
    }
}


@Serializable
data class OrderStatus(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String
) : BaseModel() {
    companion object {
        const val tableName = "order_status"

        const val ORDER_CONFIRMED = "Заказ оформлен"
        const val ORDER_DELIVERED = "Заказ доставлен"
        const val ORDER_TAKEN = "Заказ получен"
    }
}

@Serializable
data class ProductSize(
    @SerialName("id") var id: Int,
    @SerialName("product_id") var productId: String,
    @SerialName("quantity") var quantity: Int,
    @SerialName("size_rus") var sizeRus: Double,
    @SerialName("color") var color: String,
    @SerialName("image") var image: String?,
    @SerialName("stock_id") var stockId: Int
) : BaseModel() {
    companion object {
        const val tableName = "products_sizes"
    }
}

@Serializable
data class OrderDetailsView(
    @SerialName("order_id") val orderId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("quantity_in_order") val quantityInOrder: Int,
    @SerialName("product_example_id") val productExampleId: Int,
    @SerialName("product_id") val productId: String,
    @SerialName("order_date_time") val orderDateTime: Long,
    @SerialName("name") val name: String,
    @SerialName("price") val price: Double,
    @SerialName("image") val image: String,
    @SerialName("size_rus") val sizeRus: Double,
    @SerialName("color") val color: String,
    @SerialName("status") val status: String
) {
    companion object {
        const val viewName = "order_details_view"
    }
}

@Serializable
data class Notification(
    @SerialName("id") var id: Int,
    @SerialName("title") var title: String,
    @SerialName("message") var message: String,
    @SerialName("send_at") var sendAt: Long,
    @SerialName("read_at") var readAt: Long?,
    @SerialName("user_id") var userId: String
) : BaseModel() {
    companion object {
        const val tableName = "notifications"
    }
}
