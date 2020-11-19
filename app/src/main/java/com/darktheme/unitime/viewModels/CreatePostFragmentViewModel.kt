package com.darktheme.unitime.viewModels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Base64
import android.view.LayoutInflater
import android.widget.*
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.SearchResponseListObj
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException


class CreatePostFragmentViewModel(val context: Context, val fragment: Fragment) : BaseObservable() {

    companion object {
        val NOTHING = 0
        val IMAGE = 2
        val FILE = 3
    }


    val facultyList = ArrayList<String>().toMutableList()
    var facultyAdapter: ArrayAdapter<String>? = null
    val departmentList = ArrayList<String>().toMutableList()
    var departmentAdapter: ArrayAdapter<String>? = null
    val courseList = ArrayList<String>().toMutableList()
    var courseAdapter: ArrayAdapter<String>? = null

    var isQuestionEnabled : ObservableBoolean? = null  // Always enabled
    var isSuggestionEnabled : ObservableBoolean? = null  // Always enabled
    var isTestEnabled : ObservableBoolean? = null  // // only with faculty, department, course selected
    var isSummaryEnabled : ObservableBoolean? = null  // only with faculty, department, course selected
    var isMemeEnabled : ObservableBoolean? = null  // Always enabled
    var isCreatePostEnabled : ObservableBoolean? = null  // Always enabled

    var currentCheckbox : CheckBox? = null  // Always enabled
    var questionCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { compoundButton, b ->
                    checkBoxListen(b, questionCheckbox, IMAGE)
                }
            }
        }
    var suggestionCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { compoundButton, b ->
                    checkBoxListen(b, suggestionCheckbox, IMAGE)
                }
            }
        }
    var testCheckbox : CheckBox? = null  // // only with faculty, department, course selected
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { compoundButton, b ->
                    checkBoxListen(b, testCheckbox, FILE)
                }
            }
        }
    var summaryCheckbox : CheckBox? = null  // only with faculty, department, course selected
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { compoundButton, b ->
                    checkBoxListen(b, summaryCheckbox, FILE)
                }
            }
        }
    var memeCheckbox : CheckBox? = null  // Always enabled
        set(value) {
            field = value
            if (field != null) {
                field!!.setOnCheckedChangeListener { compoundButton, b ->
                    checkBoxListen(b, memeCheckbox, IMAGE)
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
        isQuestionEnabled = ObservableBoolean(true)
        isSuggestionEnabled = ObservableBoolean(true)
        isTestEnabled = ObservableBoolean(false)
        isSummaryEnabled = ObservableBoolean(true)
        isMemeEnabled = ObservableBoolean(true)
        isCreatePostEnabled = ObservableBoolean(true)
    }

    var type = NOTHING

    fun onTestClick() {
        if (!testCheckbox!!.isEnabled) {
            //TODO show error: you have to select a course
        }
    }

    fun onSummaryClick() {
        if (!testCheckbox!!.isEnabled) {
            //TODO show error: you have to select a course
        }
    }

    fun setFaculty(): ArrayAdapter<String>{
        facultyList.add("None")
        facultyAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, facultyList)
        val response = setList(facultyList, facultyAdapter!!)
        //requestHierarchy(null, response)
        return facultyAdapter!!
    }

    fun setDepartment(): ArrayAdapter<String>{
        departmentAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, departmentList)
        loadDepartments(null)
        return departmentAdapter!!
    }

    fun loadDepartments(faculty: String?) {
        departmentList.clear()
        departmentList.add("None")
        departmentAdapter!!.notifyDataSetChanged()
        if (faculty == null) {
            return
        }
        val response = setList(departmentList, departmentAdapter!!)
        //requestHierarchy(null, response)
    }

    fun setCourse(): ArrayAdapter<String>{
        courseAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, courseList)
        loadCourses(null)
        return courseAdapter!!
    }

    fun loadCourses(department: String?) {
        courseList.clear()
        courseList.add("None")
        courseAdapter!!.notifyDataSetChanged()
        if (department == null) {
            return
        }

        // TODO make sure department is faculty/department
        val response = setList(courseList, courseAdapter!!)
        //requestHierarchy(department, response)
    }

    //TODO change SearchResponseListObj to hierarchy request
    fun setList(lst: MutableList<String>, adapter: ArrayAdapter<String>) : (Call<SearchResponseListObj>?, Response<SearchResponseListObj>?) -> Unit {
        return { call: Call<SearchResponseListObj>?, response: Response<SearchResponseListObj>? ->
            val code = response!!.code()
            if (code == 200) {
                //TODO get response list
                //lst.addAll(response.body())

                adapter.notifyDataSetChanged()
            }
        }
    }

    fun facultySelected(postition: Int) {
        var fac : String? = null
        if (postition == 0) {
            //none
        } else {
            //name
            fac = facultyList[postition]
        }
        loadDepartments(fac)
    }


    fun departmentSelected(postition: Int) {
        var dep : String? = null
        if (postition == 0) {
            //none
        } else {
            //name
            dep = facultyList[postition]
        }
        loadCourses(dep)
    }


    fun courseSelected(postition: Int) {
        if (postition == 0) {
            //none
        } else {
            //name
            isTestEnabled?.set(true)
        }
    }
}