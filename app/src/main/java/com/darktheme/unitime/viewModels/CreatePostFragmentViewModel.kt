package com.darktheme.unitime.viewModels

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean

class CreatePostFragmentViewModel : BaseObservable() {

    var isQuestionEnabled : ObservableBoolean? = null
    var isSuggestionEnabled : ObservableBoolean? = null
    var isTestEnabled : ObservableBoolean? = null
    var isSummaryEnabled : ObservableBoolean? = null

    init {
        isQuestionEnabled = ObservableBoolean(true)
        isSuggestionEnabled = ObservableBoolean(true)
        isTestEnabled = ObservableBoolean(false)
        isSummaryEnabled = ObservableBoolean(false)
    }

    fun facultySelected() {

    }

    fun facultyDeselected() {

    }

    fun departmentSelected() {

    }

    fun departmentDeselected() {

    }

    fun courseSelected() {
        isTestEnabled?.set(true)
    }

    fun courseDeselected() {
        isTestEnabled?.set(false)
    }


}