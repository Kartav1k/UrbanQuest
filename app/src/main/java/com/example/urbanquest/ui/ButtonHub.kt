package com.example.urbanquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.urbanquest.R

class ButtonHub: Fragment() {

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
        val buttonToFragment4: Button = view.findViewById(R.id.dataBut)
        buttonToFragment1.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_cameraFragment);
        }
        buttonToFragment2.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_recView);
        }
        buttonToFragment3.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_screenOfLogin);
        }
        buttonToFragment4.setOnClickListener {
            findNavController().navigate(R.id.action_buttonHub_to_screenOfData);
        }
    }
}