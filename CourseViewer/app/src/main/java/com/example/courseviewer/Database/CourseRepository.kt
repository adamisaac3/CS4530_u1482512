package com.example.courseviewer.Database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CourseRepository(val scope: CoroutineScope, private val dao: CourseDao){
    val allCourses: Flow<List<Course>> = dao.getAllCourses()

    fun insert(course: Course){
        scope.launch{
            dao.insert(course)
        }

    }

    fun update(course: Course){
        scope.launch{
            dao.update(course)
        }
    }

    fun delete(course: Course){
        scope.launch{
            dao.delete(course)
        }
    }

}