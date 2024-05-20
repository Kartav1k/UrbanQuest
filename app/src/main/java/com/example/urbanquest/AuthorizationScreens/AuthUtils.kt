package com.example.urbanquest.AuthorizationScreens

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

fun isLoginEmpty(login: String): Boolean {
    return login.isNotBlank()
}
fun isLoginCorrect(login: String): Boolean {
    val hasLetter = login.any { it.isLetter() }
    val lengthValid = login.length in 4..16
    return hasLetter && lengthValid
}

fun isEmailEmpty(email: String): Boolean {
    return email.isNotBlank()
}

fun isEmailCorrect(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordCorrect(password: String): Boolean {
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    val lengthValid = password.length in 6..12
    return hasLetter && hasDigit && lengthValid
}

fun registrationUser(email: String, login: String, password: String) {
    val auth: FirebaseAuth = Firebase.auth
    val firebaseRef: DatabaseReference = FirebaseDatabase
        .getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("users")
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Registration", "Create user with email: success")
                val user = auth.currentUser
                val userID = user?.uid
                val userData = User(userID, login, email)
                if (userID != null) {
                    firebaseRef.child(userID).setValue(userData)
                        .addOnCompleteListener {
                            Log.d("RegistrationProfileInDB", "Success")
                        }
                        .addOnFailureListener { exception ->
                            Log.w("RegistrationProfileInDB", "Error", exception)
                        }
                }
            } else {
                Log.w("Registration", "Create user with email: failure", task.exception)
            }
        }
}
