package com.example.shopmobileapplication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

abstract class BaseModel {
    abstract val tableName: String
}

@Serializable
data class User(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("image") var image: String?,
    override val tableName: String = "users"
): BaseModel()

@Serializable
data class Product(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("category") var category: Int,
    @SerialName("description") var description: String,
    @SerialName("price") var price: Double,
    @SerialName("image") var image: String?,
    @SerialName("seller_id") var sellerId: Int,
    override val tableName: String = "products"
): BaseModel()

@Serializable
data class Bucket(
    @SerialName("user_id") var userId: String,
    @SerialName("product_id") var productId: String,
    @SerialName("quantity") var quantity: Int,
    override val tableName: String = "buckets"
): BaseModel()

@Serializable
data class Favorite(
    @SerialName("user_id") var userId: String,
    @SerialName("product_id") var productId: String,
    override val tableName: String = "favorites"
): BaseModel()

@Serializable
data class Seller(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    @SerialName("image") var image: String?,
    override val tableName: String = "sellers"
): BaseModel()

@Serializable
data class ProductCategory(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    override val tableName: String = "categories"
): BaseModel()

@Serializable
data class Store(
    @SerialName("id") var id: String,
    @SerialName("name") var name: String,
    @SerialName("address") var address: String,
    @SerialName("seller_id") var sellerId: String,
    override val tableName: String = "stores"
): BaseModel()

@Serializable
data class Order(
    @SerialName("id") var id: String,
    @SerialName("user_id") var userId: String,
    @SerialName("product_id") var productId: String,
    @SerialName("quantity") var quantity: Int,
    @SerialName("order_date") var orderDate: String,
    override val tableName: String = "orders"
): BaseModel()

