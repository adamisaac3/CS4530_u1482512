package com.example.funfacts.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FunFactEntity::class], version=1, exportSchema = false)
abstract class FunFactDatabase : RoomDatabase(){
    abstract fun funFactDao(): FunFactDao
}