package com.example.urbanquest.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.urbanquest.R

class ButtonHub: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_button_hub, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image1: ImageView = view.findViewById(R.id.imageView)
        val buttonToFragment1: Button = view.findViewById(R.id.cameraBut)
        val buttonToFragment2: Button = view.findViewById(R.id.listBut)
        val buttonToFragment3: Button = view.findViewById(R.id.photoBut)
        //val buttonToFragment4: Button = view.findViewById(R.id.dataBut)
        buttonToFragment1.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_cameraFragment);
        }
        buttonToFragment2.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_recView);
        }
        buttonToFragment3.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_screenOfLogin);
        }
        /*buttonToFragment4.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_);
        }*/
    }
}
/*
package com.example.urbanquest.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.urbanquest.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        checkCameraPermission()
        checkStoragePermission()
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val previewView: PreviewView = view.findViewById(R.id.viewFinder)// Настройка PreviewView для предварительного просмотра
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA //Можно и DEFAULT_FRONT_CAMERA Создание CameraSelector для выбора камеры (например, задней камеры)
            try {
                cameraProvider.bindToLifecycle(this, cameraSelector, preview) // Привязка превью к камере
            } catch (e: Exception) {
                Log.d("Camera","Ошибка камеры")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
        val butSave: Button =view.findViewById(R.id.buttonSave)
        butSave.setOnClickListener{
            saveDate()
        }
        super.onViewCreated(view, savedInstanceState)
    }
    fun saveDate(){
        val date=SimpleDateFormat("yyyy-MM-dd HH:mm:ss\n").format(Date())
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photos")// Путь к папке photos на внутреннем хранилище устройства
        if(!dir.exists()) {
            if (dir.mkdir()) {
                val fileName = File(dir, "date.txt")
                fileName.createNewFile()
                try {
                    Log.d("Camera", "Запись сделана")
                    fileName.writeText(date)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("Camera", "Запись не удалась")
                }
            }
            else{
                Log.d("Camera", "Папку создать не удалось")
            }
        }
        else{
            val fileName = File(dir, "date.txt")
            try {
                Log.d("Camera", "Запись сделана")
                fileName.appendText((date))
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("Camera", "Запись не удалась")
            }
        }
    }
    private fun checkCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
            false
        }
    }

    private fun checkStoragePermission(): Boolean {
        return if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            false
        }
    }
} */