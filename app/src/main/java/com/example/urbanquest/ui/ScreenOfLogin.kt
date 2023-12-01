package com.example.urbanquest.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.urbanquest.R
import com.example.urbanquest.ui.state_holder.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ScreenOfLogin : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_screen_of_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(RegistrationViewModel::class.java)
        val textName: TextView =view.findViewById(R.id.ViewName)
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            textName.text=it.name
        }
        val but: Button =view.findViewById(R.id.but)
        //val link: EditText=view.findViewById(R.id.editText)
        //val linkSTR: String=link.toString()
        val image: ImageView =view.findViewById(R.id.imageView)
        val imageUrl = "https://sportishka.com/uploads/posts/2022-11/1667563774_8-sportishka-com-p-priroda-kitaya-krasivo-8.jpg"
        but.setOnClickListener {
            if (imageUrl.isNotBlank()) {

                downloadImageAndSave(imageUrl, image)
            }
        }
    }
    private fun downloadImageAndSave(imageUrl: String, imageView: ImageView) {
        GlobalScope.launch(Dispatchers.IO) {
            val bitmap = downloadImage(imageUrl)
            val savedImagePath = saveImageToDisk(bitmap)
            displayImage(savedImagePath, imageView)
        }
    }

    private fun downloadImage(imageUrl: String): Bitmap {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        val inputStream = connection.inputStream
        return BitmapFactory.decodeStream(inputStream)
    }

    private suspend fun saveImageToDisk(bitmap: Bitmap): String {
        val fileName = "downloaded_image.png"
        val imageFile = File(requireContext().getExternalFilesDir(null), fileName)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return imageFile.absolutePath
    }

    private suspend fun displayImage(imagePath: String, imageView: ImageView) {
        withContext(Dispatchers.Main) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)
        }
    }
}