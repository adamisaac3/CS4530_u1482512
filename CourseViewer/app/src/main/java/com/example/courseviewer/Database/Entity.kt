package com.example.courseviewer.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="courses")
data class Course(
    @PrimaryKey(autoGenerate=true) val id: Int = 0,
    val department: String,
    val courseNum: Int,
    val location: String
)