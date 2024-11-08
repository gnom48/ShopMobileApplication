package com.example.shopmobileapplication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

abstract class BaseModel {

}

@Serializable
data class User(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("image") var image: String?
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
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("address") var address: String,
    @SerialName("seller_id") var sellerId: Int
) : BaseModel() {
    companion object {
        const val tableName = "stores"
    }
}

@Serializable
data class Order(
    @SerialName("id") var id: String,
    @SerialName("user_id") var userId: String,
    @SerialName("product_example_id") var productExampleId: Int,
    @SerialName("quantity") var quantity: Int,
    @SerialName("order_date") var orderDate: String
) : BaseModel() {
    companion object {
        const val tableName = "orders"
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
    @SerialName("store_id") var storeId: Int
) : BaseModel() {
    companion object {
        const val tableName = "products_sizes"
    }
}
