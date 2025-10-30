package com.example.funfacts.room


import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "fun_facts")
data class FunFactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0,
    val text: String,
    val source: String,
    val sourceUrl: String,
    val language: String,
    val permalink: String
)