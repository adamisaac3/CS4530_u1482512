package com.example.courseviewer

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import androidx.room.*
import com.example.courseviewer.Database.CourseDatabase
import com.example.courseviewer.Database.CourseRepository

class CourseApplication : Application(){

    val scope = CoroutineScope(SupervisorJob())

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            klass= CourseDatabase::class.java,
            "course_database",
        ).build()
    }

    val courseRepository by lazy { CourseRepository(scope, db.courseDao()) }

}