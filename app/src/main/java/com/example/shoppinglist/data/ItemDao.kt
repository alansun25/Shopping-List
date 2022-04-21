package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {
    @Query("SELECT * FROM shoppingList")
    fun getAll() : LiveData<List<Item>>

    @Insert
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("DELETE FROM shoppingList")
    fun deleteAll()

    @Query("DELETE FROM shoppingList WHERE bought")
    fun deleteBought()

    @Update
    fun updateItem(item: Item)
}