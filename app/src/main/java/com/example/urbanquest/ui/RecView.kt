package com.example.urbanquest.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanquest.R
import com.example.urbanquest.data.models.RecViewElementaOfData
import java.io.File


class RecView : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        checkStoragePermission()
        return inflater.inflate(R.layout.fragment_rec_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recView: RecyclerView=view.findViewById(R.id.recyclerview)
        val dateList = loadDatesFromFile()
        val adapter = DateTimeAdapter(dateList)
        recView.adapter=adapter
        recView.layoutManager=LinearLayoutManager(requireContext())
        Log.d("RecView", "Список создан")
    }
    private fun loadDatesFromFile(): List<RecViewElementaOfData> {
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photos")
        val fileName = File(dir, "date.txt")
        val dateItems = mutableListOf<RecViewElementaOfData>()
        if (fileName.exists()) {
            val lines = fileName.readLines()
            for (line in lines) {
                dateItems.add(RecViewElementaOfData(line))
            }
        }
        return dateItems
    }
    private fun checkStoragePermission(): Boolean {
        return if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            false
        }
    }
}