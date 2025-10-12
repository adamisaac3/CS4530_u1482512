package com.example.courseviewer.Database
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao{

    @Delete
    suspend fun delete(course: Course)

    @Insert
    suspend fun insert(course: Course)

    @Update
    suspend fun update(course: Course)

    @Query("select * from courses")
    fun getAllCourses() : Flow<List<Course>>
}