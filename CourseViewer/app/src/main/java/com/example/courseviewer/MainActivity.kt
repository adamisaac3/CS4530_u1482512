package com.example.courseviewer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.courseviewer.ui.theme.ComposeDEMOTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.courseviewer.Database.Course



/**
 * The shell of the app.
 * Connects the adding button and the list of courses below it.
 */
@Composable
fun CourseApp(vm: CourseViewModel) {
    var popup by remember{mutableStateOf<Pair<Int, Course?>>(-1 to null)}
    val showPopup = popup.second != null || popup.first == 0

    if(showPopup){
        Dialog(onDismissRequest = { popup = -1 to null }) {
            PopupCourseBox(
                turnOffScreen = { popup = -1 to null },
                vm = vm,
                functionType = popup.first,
                course = popup.second
            )
        }
    }

    val courses by vm.courses.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text("Course Viewer", fontWeight=FontWeight.Bold, fontSize=24.sp)
            Button(
                onClick = { popup = 0 to null }
            ){
                Icon(Add, contentDescription="Add Course Icon")
            }

        }
        Row{
            CourseList(courses, onEdit = {c -> popup = 1 to c}, onDelete = {course -> vm.deleteCourse(course)})
        }

    }
}

/**
 * @param turnOffScreen Function passed in so when the back button is clicked you go back to the main screen
 * @param vm: The ViewModel
 * @param functionType: This is used to determine between whether the popup box is a
 * editing box, or a add course box. Since theres only 2 options I just hardcoded values.
 * @param course: This is an optional parameter. It contains either the course if the user
 * wants to edit, or just null if wanting to add a new course.
 *
 * This component is the box that pops up when the user wants to add a new course or edit course info.
 */
@Composable
fun PopupCourseBox(turnOffScreen: () -> Unit, vm: CourseViewModel, functionType: Int, course: Course? = null){
    var department by remember {mutableStateOf(course?.department ?: "")}
    var number by remember{mutableStateOf(course?.courseNum?.toString() ?: "")}
    var loc by remember {mutableStateOf(course?.location ?: "")}



    fun addClassOnClick(){
        val courseNumber = number.toIntOrNull()
        
        if(courseNumber != null && !department.isEmpty() && !loc.isEmpty()){
            vm.addCourse(department, courseNumber, loc)
            department = ""
            number = ""
            loc = ""
            turnOffScreen()
        }
    }

    fun editClassOnClick(){
        val courseNumber = number.toIntOrNull()

        if(course != null && courseNumber != null){
            val updatedCourse = course!!.copy(
                courseNum = courseNumber,
                department = department,
                location = loc
            )


            vm.editCourse(updatedCourse)
            turnOffScreen()
        }
    }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.zIndex(10f)
        .background(color=Color.White, shape=RoundedCornerShape(12.dp)).width(300.dp).height(375.dp).padding(5.dp)

    , contentAlignment = Alignment.Center,
        ){
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)){
            Row{
                OutlinedTextField(value = department,
                    onValueChange = {input: String -> department = input},
                    label= { Text("Department") })
            }
            Row {
                OutlinedTextField(keyboardOptions =  KeyboardOptions(keyboardType = KeyboardType.Number) ,
                    value = number,
                    onValueChange={ input ->
                        if(input.length <= 9 && input.all{it.isDigit()}){
                            number = input
                        }
                    },
                    label = { Text("Number")}
                )
            }
            Row{
                OutlinedTextField(value=loc,
                    onValueChange = {input -> loc = input},
                    label = {Text("Location")})
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top=25.dp)
            , horizontalArrangement= Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ){
                Button( onClick={turnOffScreen()}){
                    Text("Back")
                }

                Button( onClick= {
                    if(functionType == 0) addClassOnClick() else editClassOnClick()
                }){
                    Text(if(functionType == 0) "Add Course" else "Save Changes")
                }
            }
        }
    }
}

/**
 * @param course: The course object that contains the info.
 * @param onEdit: The function passed that is also passed to child elements for editing
 * @param onDelete: The functinon passed that is also passed to child elements for deleting
 *
 * This component is used as the list option on the app.
 */
@Composable
fun CourseItem(course: Course, onEdit: (Course) -> Unit, onDelete: (Course) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onEdit(course) }
            .background(color = Color.LightGray, shape = RoundedCornerShape(6.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = course.department,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 8.dp).weight(1f, false)
            )
            Text(
                text = course.courseNum.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Button(
            onClick = { onDelete(course) },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(ChromeClose, contentDescription = "Delete Icon")
        }
    }
}


/**
 * @param courses The list of courses in the map
 * @param onEdit The function passed to edit a course on click.
 * @param onDelete The function to delete a course on click.
 *
 * This component is used to display the courses in a lazy column list.
 * Will only display classes or courses that are within render frame.
 */
@Composable
fun CourseList(courses: List<Course>, onEdit: (Course) -> Unit, onDelete: (Course) -> Unit){
    if(courses.isEmpty()) {
        Text(text="Courses List is currently empty.", modifier= Modifier.padding(top=100.dp))
    }
    else{
        LazyColumn {
            items(courses.toList()) { c ->
                CourseItem(c, onEdit, onDelete)
            }
        }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDEMOTheme {
                val vm: CourseViewModel = viewModel(factory=CourseViewModelProvider.Factory)
                CourseApp(vm)
            }
        }
    }
}
