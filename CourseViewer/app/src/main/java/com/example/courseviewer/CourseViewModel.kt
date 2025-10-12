package com.example.courseviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.courseviewer.Database.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.courseviewer.Database.Course


/**
 * The view model class. This is used to change data on the backend.
 * The views only use the readable data and never directly interact with the mutable collections.
 */
class  CourseViewModel(private val repository: CourseRepository) : ViewModel(){

    val courses: StateFlow<List<Course>> = repository.allCourses.stateIn(scope=repository.scope, started= SharingStarted.WhileSubscribed(), initialValue=emptyList() )

    fun addCourse(dep: String, courseNum: Int, location: String){
       viewModelScope.launch{
           repository.insert(Course(department=dep, courseNum = courseNum, location = location))
       }
    }


    fun deleteCourse(course: Course){
        viewModelScope.launch{
            repository.delete(course)
        }
    }

    fun editCourse(course: Course){
        viewModelScope.launch{
            repository.update(course)
        }
    }
}

object CourseViewModelProvider {
    val Factory = viewModelFactory{
        initializer{
            CourseViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CourseApplication).courseRepository
            )
        }
    }
}