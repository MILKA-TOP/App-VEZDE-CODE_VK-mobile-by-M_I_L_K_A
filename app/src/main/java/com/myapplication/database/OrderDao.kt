package com.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapplication.DATABASE_NAME
import com.myapplication.OrderInformation
import com.myapplication.OrderStatus


@Dao
interface OrderDao {

    @Query(value = "SELECT * FROM $DATABASE_NAME")
    fun getAll(): List<OrderInformation>

    @Query("SELECT * FROM $DATABASE_NAME WHERE orderNumber = :id")
    fun getById(id: Int): OrderInformation?

    @Query("SELECT * FROM $DATABASE_NAME WHERE status = :orderStatus")
    fun getInProgressOrder(orderStatus: OrderStatus): OrderInformation?

    @Insert
    fun insertOrder(order: OrderInformation)

    @Update
    fun updateOrder(order: OrderInformation)

    @Query("DELETE FROM $DATABASE_NAME")
    fun deleteAll()

}