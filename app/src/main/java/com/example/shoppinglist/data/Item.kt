package com.example.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shoppingList")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    var category: Int,
    var name: String,
    var desc: String,
    var price: String,
    var bought: Boolean
) : Serializable
