package com.darktheme.unitime.views.Fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.AttachmentObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.CreatePostObj
import com.darktheme.unitime.views.Activities.MainPageActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.StringBuilder


class CreatePostFileFragment : Fragment() {

    val LOAD_PDF = 1
    var fileBase64: String? = null
    var flair: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_create_post_file, container, false)
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        flair = requireArguments().getString("flair")
        view.findViewById<Button>(R.id.upload).setOnClickListener {
            choosePdf()
        }
        return view
    }

    private fun choosePdf() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), LOAD_PDF)
    }

    fun loadFile(data: Uri) {


        val path = data.path!!
        val filename = data.lastPathSegment
        requireView().findViewById<TextView>(R.id.file_name).text =  filename
        requireView().findViewById<TextView>(R.id.file_name).visibility =  View.VISIBLE
        fileBase64 = convertFileToBase64(data)
    }

    fun convertFileToBase64(fileUri: Uri): String? {

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
        if ((fileBase64 != null) && (!requireView().findViewById<TextView>(R.id.file_name).text.isNullOrEmpty())) {
            var name = requireView().findViewById<TextView>(R.id.file_name).text.toString()
            if (name.endsWith(".pdf")) {
                name = StringBuilder(name).append(".pdf").toString()
            }
            CreatePostObj((requireActivity() as MainPageActivity).email!!, flair!!, getText(), AttachmentObj(name, fileBase64!!))
            // TODO send it to the server
        }
    }

    fun getText() : String {
        return ""
    }
}