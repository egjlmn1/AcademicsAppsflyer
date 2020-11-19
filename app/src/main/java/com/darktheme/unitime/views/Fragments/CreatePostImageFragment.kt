package com.darktheme.unitime.views.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.AttachmentObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.CreatePostObj
import com.darktheme.unitime.views.Activities.MainPageActivity
import java.io.ByteArrayOutputStream
import java.io.InputStream


class CreatePostImageFragment : Fragment() {

    val LOAD_IMAGE = 100
    var fileBase64: String? = null
    var flair: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_create_post_image, container, false)
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        flair = requireArguments().getString("flair")
        view.findViewById<Button>(R.id.upload).setOnClickListener {
            chooseImage()
        }
        return view
    }

    fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        println("result")
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE)
    }

    fun convertToBase64(image: Uri): String? {
        val imageStream: InputStream? = requireActivity().contentResolver.openInputStream(image)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun loadImage(data: Uri) {
        println("image loading")
        requireView().findViewById<ImageView>(R.id.image).setImageURI(data)
        fileBase64 = convertToBase64(data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("image loading")
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == RESULT_OK) && (data != null)) {
            if (requestCode == LOAD_IMAGE) {
                loadImage(data.data!!)
            }
        }
    }

    fun createPost() {
        if (fileBase64 != null) {
            CreatePostObj((requireActivity() as MainPageActivity).email!!, flair!!, getText(), AttachmentObj("PNG", fileBase64!!))
            // TODO send it to the server
        }
    }

    fun getText() : String {
        return ""
    }
}