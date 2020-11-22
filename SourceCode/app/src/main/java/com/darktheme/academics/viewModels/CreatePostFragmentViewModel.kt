package com.darktheme.academics.viewModels

import android.content.Context
import android.view.View
import android.widget.*
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import com.darktheme.academics.R
import com.darktheme.academics.models.Retrofit.JsonObjects.FlairObj
import com.darktheme.academics.models.Retrofit.JsonObjects.IdListRequest
import com.darktheme.academics.models.Retrofit.JsonObjects.SearchResponseListObj
import com.darktheme.academics.models.Retrofit.PostLoader
import com.darktheme.academics.models.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception


class CreatePostFragmentViewModel(val context: Context, val fragment: Fragment, val errorText: TextView) : BaseObservable() {

    companion object {
        val NOTHING = 0
        val IMAGE = 2
        val FILE = 3
    }

//    var faculty = ""
//    var department = ""
//    var course = ""
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
                        errorText.visibility = View.VISIBLE
                        checkBoxListen(false, testCheckbox, FILE)
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
                        errorText.visibility = View.VISIBLE
                        checkBoxListen(false, summaryCheckbox, FILE)
                    } else {
                        checkBoxListen(b, summaryCheckbox, FILE)
                        if (b) {
                            flair = "summary"
                        }
                    }
                }
            }
        }
    var socialCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { _, b ->
                    checkBoxListen(b, socialCheckbox, IMAGE)
                    if (b) {
                        flair = "social"
                    }
                }
            }
        }

    fun checkBoxListen(b: Boolean, checkBox: CheckBox?, t: Int) {
        if (b) {
            errorText.visibility = View.GONE
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

    fun getFaculty(): String {
        try {
            val c = facultySpinner!!.selectedItem.toString()
            if (c.equals("None")) {
                return ""
            }
            return c
        } catch (e: Exception) {
            return ""
        }
    }

    fun getDepartment(): String {
        try {
            val c = departmentSpinner!!.selectedItem.toString()
            if (c.equals("None")) {
                return ""
            }
            return c
        } catch (e: Exception) {
            return ""
        }
    }

    fun getCourse(): String {
        try {
                val c = courseSpinner!!.selectedItem.toString()
            if (c.equals("None")) {
                return ""
            }
            return c
        } catch (e: Exception) {
            return ""
        }
    }

    fun setFaculty(fac: String, spinner: Spinner) {
        facultyList.add("None")
        facultySpinner = spinner
        facultySpinner!!.adapter = ArrayAdapter(context, R.layout.text_item, facultyList)
        val response = setList(fac, facultyList, facultySpinner!!)
        IdListRequest(RetrofitClient.getInstance()!!).folders("", fullFalir, response, myOnFaliure)
    }

    fun setDepartment(dep: String, path: String, spinner: Spinner) {
        departmentList.add("None")
        departmentSpinner = spinner
        departmentSpinner!!.adapter = ArrayAdapter(context, R.layout.text_item, departmentList)
        if (path.isNotEmpty()) {
            val response = setList(dep, departmentList, departmentSpinner!!)
            IdListRequest(RetrofitClient.getInstance()!!).folders(path, fullFalir, response, myOnFaliure)
        }
    }

    fun loadDepartments() {
        departmentList.clear()
        departmentList.add("None")
        (departmentSpinner!!.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        if (getFaculty().isEmpty()) {
            return
        }
        println("loading department: " + getFaculty() + "/" + getDepartment())
        val response = setList("", departmentList, departmentSpinner!!)
        IdListRequest(RetrofitClient.getInstance()!!).folders(getFaculty(), fullFalir, response, myOnFaliure)
    }

    fun setCourse(cour: String, path:String, spinner: Spinner) {
        courseList.add("None")
        courseSpinner = spinner
        courseSpinner!!.adapter = ArrayAdapter(context, R.layout.text_item, courseList)
        if (path.isNotEmpty()) {
            val response = setList(cour, courseList, courseSpinner!!)
            IdListRequest(RetrofitClient.getInstance()!!).folders(path, fullFalir, response, myOnFaliure)
        }
    }

    fun loadCourses() {
        courseList.clear()
        courseList.add("None")
        println("loading "+ getFaculty()+'/'+getDepartment())

        (courseSpinner!!.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        if (getDepartment().isEmpty()) {
            return
        }

        val response = setList("", courseList, courseSpinner!!)
        IdListRequest(RetrofitClient.getInstance()!!).folders(getFaculty()+'/'+getDepartment(), fullFalir, response, myOnFaliure)
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
                    println("VALUE IS: "+ value)
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
        loadDepartments()
    }


    fun departmentSelected(postition: Int) {
        loadCourses()
    }


    fun courseSelected(postition: Int) {
        if (postition == 0) {
            if (testCheckbox!!.isChecked) {
                testCheckbox!!.isChecked = false
            }
            if (summaryCheckbox!!.isChecked) {
                summaryCheckbox!!.isChecked = false
            }
        }
    }

    fun getPath(): String {
        var path = ""
        if (getFaculty().isEmpty()) {
            path = ""
        } else {
            if (getDepartment().isEmpty()) {
                path = getFaculty()
            } else {
                if (getCourse().isEmpty()) {
                    val pathArr = listOf<String>(getFaculty(), getDepartment())
                    path = pathArr.joinToString(separator = "/")
                } else {
                    val pathArr = listOf<String>(getFaculty(), getDepartment(), getCourse())
                    path = pathArr.joinToString(separator = "/")
                }
            }
        }
        return path
    }
}