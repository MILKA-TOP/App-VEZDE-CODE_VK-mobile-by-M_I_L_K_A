package com.myapplication

import android.app.Application
import androidx.room.Room
import com.myapplication.database.AppDatabase


class MainApp : Application() {

    lateinit var dataBase: AppDatabase
    override fun onCreate() {
        super.onCreate()
        instance = this

        dataBase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build();
    }


    companion object {
        lateinit var instance: MainApp
            private set
    }


}