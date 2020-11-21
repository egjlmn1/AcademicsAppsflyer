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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.darktheme.unitime.R
import com.darktheme.unitime.models.Retrofit.JsonObjects.AttachmentObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.CreatePostObj
import com.darktheme.unitime.models.Retrofit.JsonObjects.CreatePostRequest
import com.darktheme.unitime.models.Retrofit.JsonObjects.PostObj
import com.darktheme.unitime.models.Retrofit.RetrofitClient
import com.darktheme.unitime.views.Activities.MainPageActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream


class CreatePostImageFragment : Fragment() {

    val LOAD_IMAGE = 100
    var imageBase64: String? = null
    var path: String? = null
    var flair: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_create_post_image, container, false)
        val activity = requireActivity() as MainPageActivity
        activity.hideNavBar()
        flair = requireArguments().getString("flair")
        path = requireArguments().getString("path")
        view.findViewById<Button>(R.id.upload).setOnClickListener {
            chooseImage()
        }
        view.findViewById<Button>(R.id.create_post_btn).setOnClickListener {
            createPost()
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
        imageBase64 = convertToBase64(data)
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
        if ((requireActivity() as MainPageActivity).user_id == null ||
            (requireActivity() as MainPageActivity).email == null) {
            Toast.makeText(requireContext(),"Error occurred", Toast.LENGTH_SHORT).show();
            return
        }
        if (imageBase64 != null) {
            val createdPost = CreatePostObj(PostObj("", "image", flair!!, path!!, "", (requireActivity() as MainPageActivity).user_id.toString(), (requireActivity() as MainPageActivity).email.toString(), getText(), AttachmentObj("", "png")),
            imageBase64!!)

            println("Posting post image")
            CreatePostRequest(RetrofitClient.getInstance()!!).post(createdPost, onResponse, onFailure)
            requireView().findViewById<Button>(R.id.create_post_btn).isEnabled = false
            (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_to_home)
        } else if (getText().isNotEmpty()) {
            val createdPost = CreatePostObj(PostObj("", "text", flair!!, path!!, "", (requireActivity() as MainPageActivity).user_id.toString(), (requireActivity() as MainPageActivity).email.toString(), getText(), null),
            null)
            println("Posting post image")
            CreatePostRequest(RetrofitClient.getInstance()!!).post(createdPost, onResponse, onFailure)
            requireView().findViewById<Button>(R.id.create_post_btn).isEnabled = false
            (requireActivity() as MainPageActivity).navController!!.navigate(R.id.action_to_home)
        } else {
            Toast.makeText(requireContext(), "At least one field needs to be filled", Toast.LENGTH_SHORT).show()
        }
    }

    fun getText() : String {
        return requireView().findViewById<EditText>(R.id.text_post).text.toString().trim()
    }

    val onFailure = {call: Call<ResponseBody>?, t: Throwable? ->
        println(t!!.message)
        Toast.makeText(requireContext(), "Failed, try again later", Toast.LENGTH_SHORT).show()
    }

    val onResponse =  { call: Call<ResponseBody>?, response: Response<ResponseBody>? ->
        if (response!!.code() != 200) {
            println(response.code())
            Toast.makeText(requireContext(), "Failed, try again later", Toast.LENGTH_SHORT).show()
        }
    }
}