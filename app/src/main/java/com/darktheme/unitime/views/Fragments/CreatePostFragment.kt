package com.darktheme.unitime.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.viewModels.CreatePostFragmentViewModel
import com.darktheme.unitime.views.Activities.MainPageActivity

class CreatePostFragment : Fragment() {

    var facultySpinner : Spinner? = null
    var departmentSpinner : Spinner? = null
    var courseSpinner : Spinner? = null
    var viewModel : CreatePostFragmentViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_create_post, container, false)
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        return root
    }

    private fun setSpinners() {
        facultySpinner = requireView().findViewById<Spinner>(R.id.faculty_spinner)
        departmentSpinner = requireView().findViewById<Spinner>(R.id.department_spinner)
        courseSpinner = requireView().findViewById<Spinner>(R.id.course_spinner)

        val testList = ArrayList<String>()
        testList.add("None")
        //val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, testList)
        //facultySpinner!!.adapter = adapter
        // TODO get facult list from server and add it to the list
        // for each spinner

        setSpinnerListeners()
    }

    private fun setSpinnerListeners() {
        facultySpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position > 0) {
                    viewModel!!.facultySelected()
                } else {
                    viewModel!!.facultyDeselected()
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                viewModel!!.facultyDeselected()

            }
        })
        departmentSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position > 0) {
                    viewModel!!.departmentSelected()
                } else {
                    viewModel!!.departmentDeselected()
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                viewModel!!.departmentDeselected()

            }
        })
        courseSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position > 0) {
                    viewModel!!.courseSelected()
                } else {
                    viewModel!!.courseDeselected()
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                viewModel!!.courseDeselected()

            }
        })

    }
}