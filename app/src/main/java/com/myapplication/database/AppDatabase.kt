package com.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapplication.OrderInformation


@Database(entities = [OrderInformation::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao?
}
