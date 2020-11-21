package com.darktheme.unitime.viewModels

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.FlairObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.IdListRequest
import com.darktheme.unitime.models.Retrofit.JsonObjects.SearchResponseListObj
import com.darktheme.unitime.models.Retrofit.PostLoader
import com.darktheme.unitime.models.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class CreatePostFragmentViewModel(val context: Context, val fragment: Fragment) : BaseObservable() {

    companion object {
        val NOTHING = 0
        val IMAGE = 2
        val FILE = 3
    }

    var faculty = ""
    var department = ""
    var course = ""
    var flair = "question"

    val fullFalir = FlairObj(true,true,true,true,true)

    val facultyList = ArrayList<String>().toMutableList()
    var facultySpinner: Spinner? = null
    val departmentList = ArrayList<String>().toMutableList()
    var departmentSpinner: Spinner? = null
    val courseList = ArrayList<String>().toMutableList()
    var courseSpinner: Spinner? = null

    var isCreatePostEnabled : ObservableBoolean? = null  // Always enabled

    var currentCheckbox : CheckBox? = null  // Always enabled
    var questionCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { _, b ->
                    checkBoxListen(b, questionCheckbox, IMAGE)
                    if (b) {
                        flair = "question"
                    }
                }
            }
        }
    var suggestionCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { _, b ->
                    checkBoxListen(b, suggestionCheckbox, IMAGE)
                    if (b) {
                        flair = "suggestion"
                    }
                }
            }
        }
    var testCheckbox : CheckBox? = null  // // only with faculty, department, course selected
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { _, b ->
                    if (courseSpinner!!.selectedItem.toString().equals("None")) {
                        testCheckbox!!.isChecked = false
                    } else {
                        checkBoxListen(b, testCheckbox, FILE)
                        if (b) {
                            flair = "test"
                        }
                    }
                }
            }
        }
    var summaryCheckbox : CheckBox? = null  // only with faculty, department, course selected
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { _, b ->
                    if (courseSpinner!!.selectedItem.toString().equals("None")) {
                        summaryCheckbox!!.isChecked = false
                    } else {
                        checkBoxListen(b, summaryCheckbox, FILE)
                        if (b) {
                            flair = "summary"
                        }
                    }
                }
            }
        }
    var memeCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { _, b ->
                    checkBoxListen(b, memeCheckbox, IMAGE)
                    if (b) {
                        flair = "meme"
                    }
                }
            }
        }

    fun checkBoxListen(b: Boolean, checkBox: CheckBox?, t: Int) {
        if (b) {
            if (currentCheckbox != null) {
                if (currentCheckbox != checkBox) {
                    currentCheckbox!!.isChecked = false
                }
            }
            currentCheckbox = checkBox
            type = t
            isCreatePostEnabled!!.set(true)
        } else if (currentCheckbox == checkBox) {
            isCreatePostEnabled!!.set(false)
        }
    }

    init {
        isCreatePostEnabled = ObservableBoolean(true)
    }

    var type = NOTHING

    fun onTestClick() {
        if (!testCheckbox!!.isEnabled) {
            Toast.makeText(
                context,
                "You must select a course",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun onSummaryClick() {
        if (!testCheckbox!!.isEnabled) {
            Toast.makeText(
                context,
                "You must select a course",
                Toast.LENGTH_SHORT
            ).show()        }
    }

    fun setFaculty(fac: String, spinner: Spinner) {
        facultyList.add("None")
        faculty = fac
        facultySpinner = spinner
        facultySpinner!!.adapter = ArrayAdapter(context, R.layout.text_item, facultyList)
        val response = setList(faculty, facultyList, facultySpinner!!)
        IdListRequest(RetrofitClient.getInstance()!!).folders("", fullFalir, response, myOnFaliure)
    }

    fun setDepartment(dep: String, spinner: Spinner) {
        department = dep
        departmentSpinner = spinner
        departmentSpinner!!.adapter = ArrayAdapter(context, R.layout.text_item, departmentList)
        loadDepartments()
    }

    fun loadDepartments() {
        departmentList.clear()
        departmentList.add("None")
        (departmentSpinner!!.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        if (faculty.isEmpty()) {
            return
        }
        println("loading department: " + faculty + "/" + department)
        val response = setList(department, departmentList, departmentSpinner!!)
        IdListRequest(RetrofitClient.getInstance()!!).folders(faculty, fullFalir, response, myOnFaliure)
    }

    fun setCourse(cour: String, spinner: Spinner) {
        course = cour
        courseSpinner = spinner
        courseSpinner!!.adapter = ArrayAdapter(context, R.layout.text_item, courseList)
        loadCourses()
    }

    fun loadCourses() {
        courseList.clear()
        courseList.add("None")
        (courseSpinner!!.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        if (department.isEmpty()) {
            return
        }

        val response = setList(course, courseList, courseSpinner!!)
        IdListRequest(RetrofitClient.getInstance()!!).folders(faculty+'/'+department, fullFalir, response, myOnFaliure)
    }

    fun setList(value: String, lst: MutableList<String>, spinner: Spinner) : (Call<SearchResponseListObj>?, Response<SearchResponseListObj>?) -> Unit {
        return { _: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>? ->
            val code = response!!.code()
            if (code == 200) {
                for (result in response.body()!!.posts_ids) {
                    if (result.view_type == PostLoader.hierarchyViewType) {
                        val arr = result.content.split('/')
                        lst.add(arr[arr.lastIndex])
                    }
                }
                (spinner.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                if (value.isNotEmpty() && lst.contains(value)) {
                    spinner.setTag("init")
                    spinner.setSelection(lst.indexOf(value))
                }
            }
        }
    }

    val myOnFaliure = { _: Call<SearchResponseListObj>?, t: Throwable?->
        println(t!!.message)
        println("ERROR CreatPostFragmentViewModel")
    }

    fun facultySelected(postition: Int) {
        if (postition == 0) {
            //none
            faculty = ""
            department = ""
            course = ""
            println("Reset faculty")
        } else {
            //name
            faculty = facultyList[postition]
        }
        loadDepartments()
    }


    fun departmentSelected(postition: Int) {
        if (postition == 0) {
            //none
            department = ""
            course = ""
            println("Reset department")
        } else {
            //name
            department = departmentList[postition]
        }
        loadCourses()
    }


    fun courseSelected(postition: Int) {
        if (postition == 0) {
            //none
            course = ""
            println("Reset course")
            if (testCheckbox!!.isChecked) {
                testCheckbox!!.isChecked = false
            }
        } else {
            //name
            course = courseList[postition]
        }
    }

    fun getPath(): String {
        var path = ""
        faculty = facultySpinner!!.selectedItem.toString()
        if (faculty.equals("None")) {faculty = ""}
        department = departmentSpinner!!.selectedItem.toString()
        if (department.equals("None")) {department = ""}
        course = courseSpinner!!.selectedItem.toString()
        if (course.equals("None")) {course = ""}
        if (faculty.isEmpty()) {
            path = ""
        } else {
            if (department.isEmpty()) {
                path = faculty
            } else {
                if (course.isEmpty()) {
                    val pathArr = listOf<String>(faculty, department)
                    path = pathArr.joinToString(separator = "/")
                } else {
                    val pathArr = listOf<String>(faculty, department, course)
                    path = pathArr.joinToString(separator = "/")
                }
            }
        }
        return path
    }
}