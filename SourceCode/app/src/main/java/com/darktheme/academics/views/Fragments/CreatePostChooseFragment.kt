package com.darktheme.academics.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.darktheme.academics.R
import com.darktheme.academics.databinding.FragmentCreatePostChooseBinding
import com.darktheme.academics.viewModels.CreatePostFragmentViewModel
import com.darktheme.academics.views.Activities.MainPageActivity


class CreatePostChooseFragment : Fragment() {

    var facultySpinner : Spinner? = null
    var departmentSpinner : Spinner? = null
    var courseSpinner : Spinner? = null
    var viewModel : CreatePostFragmentViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCreatePostChooseBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_post_choose, container, false
        )
        val view: View = binding.getRoot()
        val activity = requireActivity() as MainPageActivity
        viewModel = CreatePostFragmentViewModel(activity, this)
        binding.viewModel = viewModel
        setCheckBoxes(view) // view model needed
        setButton(view)
        activity.hideNavBar()
        return view
    }

    fun setButton(view: View) {
        view.findViewById<Button>(R.id.create).setOnClickListener {
            val path = viewModel!!.getPath()
            if (viewModel!!.type == CreatePostFragmentViewModel.IMAGE) {
                val bundle = bundleOf("flair" to viewModel!!.flair, "path" to path)
                (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_nav_create_post_choose_to_nav_create_post_image, bundle)
            } else if (viewModel!!.type == CreatePostFragmentViewModel.FILE) {
                val bundle = bundleOf("flair" to viewModel!!.flair, "path" to path)
                (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_nav_create_post_choose_to_nav_create_post_file, bundle)
            }
        }
    }

    fun setCheckBoxes(view: View) {
        viewModel!!.questionCheckbox = view.findViewById<CheckBox>(R.id.question_checkbox)
        viewModel!!.suggestionCheckbox = view.findViewById<CheckBox>(R.id.suggestion_checkbox)
        viewModel!!.testCheckbox = view.findViewById<CheckBox>(R.id.test_checkbox)
        viewModel!!.summaryCheckbox = view.findViewById<CheckBox>(R.id.summary_checkbox)
        viewModel!!.memeCheckbox = view.findViewById<CheckBox>(R.id.meme_checkbox)

        view.findViewById<LinearLayout>(R.id.summary_post).setOnClickListener {
            viewModel!!.onSummaryClick()
        }
        view.findViewById<LinearLayout>(R.id.test_post).setOnClickListener {
            viewModel!!.onTestClick()
        }

        viewModel!!.questionCheckbox!!.isChecked = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinners()
    }

    private fun setSpinners() {
        facultySpinner = requireView().findViewById<Spinner>(R.id.faculty_spinner)
        departmentSpinner = requireView().findViewById<Spinner>(R.id.department_spinner)
        courseSpinner = requireView().findViewById<Spinner>(R.id.course_spinner)

        val path = (requireActivity() as MainPageActivity).currentPath
        var fac = ""
        var dep = ""
        var cou = ""
        if (path.isNotEmpty()) {
            val splitedPath = path.split('/')
            if (splitedPath.size == 1) {
                fac = path
            } else if (splitedPath.size == 2) {
                fac = splitedPath[0]
                dep = splitedPath[1]
            } else if (splitedPath.size == 3) {
                fac = splitedPath[0]
                dep = splitedPath[1]
                cou = splitedPath[2]
            }
        }

        println("fac: " + fac + " dep: " + dep + " cou: " + cou)
        viewModel!!.setFaculty(fac, facultySpinner!!)
        facultySpinner!!.setTag("after init")
        viewModel!!.setDepartment(dep, departmentSpinner!!)
        departmentSpinner!!.setTag("after init")
        viewModel!!.setCourse(cou, courseSpinner!!)
        courseSpinner!!.setTag("after init")
        setSpinnerListeners()

    }

    private fun setSpinnerListeners() {
        facultySpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (facultySpinner!!.getTag().equals("init")) {
                    facultySpinner!!.setTag("after init")
                }
                else {
                    viewModel!!.facultySelected(position)
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })
        departmentSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (departmentSpinner!!.getTag().equals("init")) {
                    departmentSpinner!!.setTag("after init")
                }
                else {
                    viewModel!!.departmentSelected(position)
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })
        courseSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (courseSpinner!!.getTag().equals("init")) {
                    courseSpinner!!.setTag("after init")
                }
                else {
                    viewModel!!.courseSelected(position)
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })
    }
}