package com.example.urbanquest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.urbanquest.Data.Models.UserData
import com.example.urbanquest.R
import com.example.urbanquest.ui.state_holder.RegistrationViewModel


class ScreenOfRegistration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_screen_of_registration, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val but: Button=view.findViewById(R.id.butOfReg)
        val surname: EditText = view.findViewById(R.id.editSurname)
        val name: EditText = view.findViewById(R.id.editName)
        val email: EditText = view.findViewById(R.id.editTextEmailAddress)
        val phone: EditText = view.findViewById(R.id.editTextPhone)
        val password: EditText = view.findViewById(R.id.editTextPassword)
        but.setOnClickListener{
            val user= UserData(surname.text.toString(), name.text.toString(), phone.text.toString(), email.text.toString(), password.text.toString())
            val viewModel = ViewModelProvider(requireActivity()).get(RegistrationViewModel::class.java)
            viewModel.registerUser(user)
            findNavController().navigate(R.id.action_screenOfRegistration_to_buttonHub)
        }
    }
}