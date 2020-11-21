package com.darktheme.academics.views.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.darktheme.academics.R
import com.darktheme.academics.models.Retrofit.JsonObjects.AttachmentObj
import com.darktheme.academics.models.Retrofit.JsonObjects.CreatePostObj
import com.darktheme.academics.models.Retrofit.JsonObjects.CreatePostRequest
import com.darktheme.academics.models.Retrofit.JsonObjects.PostObj
import com.darktheme.academics.models.Retrofit.RetrofitClient
import com.darktheme.academics.views.Activities.MainPageActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class CreatePostFileFragment : Fragment() {

    val LOAD_PDF = 1
    var fileBase64: String? = null
    var path: String? = null
    var flair: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_create_post_file, container, false)
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        flair = requireArguments().getString("flair")
        path = requireArguments().getString("path")

        view.findViewById<Button>(R.id.upload).setOnClickListener {
            choosePdf()
        }
        view.findViewById<Button>(R.id.create_post_btn).setOnClickListener {
            createPost()
        }
        return view
    }

    private fun choosePdf() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), LOAD_PDF)
    }

    private fun loadFile(data: Uri) {
        val filename = data.lastPathSegment
        requireView().findViewById<TextView>(R.id.file_name).text =  filename
        requireView().findViewById<TextView>(R.id.file_name).visibility =  View.VISIBLE
        fileBase64 = convertFileToBase64(data)
    }

    private fun convertFileToBase64(fileUri: Uri): String? {

        val file = requireActivity().contentResolver.openInputStream(fileUri)
        val bytes = file!!.readBytes()
        val base64Image =
            Base64.encodeToString(bytes, Base64.DEFAULT)
        return base64Image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) && (data != null)) {
            if (requestCode == LOAD_PDF) {
                loadFile(data.data!!)
            }
        }
    }

    fun createPost() {
        if ((requireActivity() as MainPageActivity).user_id == null ||
            (requireActivity() as MainPageActivity).email == null) {
            Toast.makeText(requireContext(),"Error occurred", Toast.LENGTH_SHORT).show()
            return
        }
        if ((fileBase64 != null) && (!requireView().findViewById<TextView>(R.id.file_name).text.isNullOrEmpty())
            && (!requireView().findViewById<TextView>(R.id.uni_edittext).text.isNullOrEmpty())
            && (!requireView().findViewById<TextView>(R.id.prof_edittext).text.isNullOrEmpty())
            && (!requireView().findViewById<TextView>(R.id.year_edittext).text.isNullOrEmpty())
        ) {
            var name = requireView().findViewById<TextView>(R.id.file_name).text.toString()
            if (!name.endsWith(".pdf")) {
                name = StringBuilder(name).append(".pdf").toString()
            }
            val createdPost = CreatePostObj(
                PostObj("", "file", flair!!, path!!, null, (requireActivity() as MainPageActivity).user_id!!.toString(), (requireActivity() as MainPageActivity).email!!, getText(), AttachmentObj(name, "pdf")),
                fileBase64
            )

            println("Posting post file")
            CreatePostRequest(RetrofitClient.getInstance()!!).post(createdPost, onResponse, onFailure)
            requireView().findViewById<Button>(R.id.create_post_btn).isEnabled = false
            (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_to_home)

        } else {
            if (fileBase64 == null) {
                Toast.makeText(requireContext(), "Upload a file", Toast.LENGTH_SHORT).show()
            }
            else if ((requireView().findViewById<TextView>(R.id.file_name).text.isNullOrEmpty()) ||
                (requireView().findViewById<TextView>(R.id.file_name).text.equals(".pdf"))) {
                Toast.makeText(requireContext(), "The file needs a name", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "All fields need to be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getText() : String {
        val splited = path!!.split("/")
        return StringBuilder("Course Name: ").append(splited[splited.lastIndex]).append("\nUniversity: ").append(requireView().findViewById<TextView>(R.id.uni_edittext).text.toString()).append("\nProfessor: ")
            .append(requireView().findViewById<TextView>(R.id.prof_edittext).text).append("\n Year: ").append(requireView().findViewById<TextView>(R.id.year_edittext).text).toString()
    }

    val onFailure = { _: Call<ResponseBody>?, _: Throwable? ->
        Toast.makeText(requireContext(), "Failed, try again later", Toast.LENGTH_SHORT).show()
    }

    val onResponse =  { _: Call<ResponseBody>?, response: Response<ResponseBody>? ->
        if (response!!.code() != 200) {
            Toast.makeText(requireContext(), "Failed, try again later", Toast.LENGTH_SHORT).show()
        }
    }
}