package com.example.urbanquest.AuthorizationScreens

import android.util.Patterns
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Различные вспомогательные функции для процесса регистрации и авторизации

fun isLoginValid(login: String): Boolean {
    return login.isNotBlank() && login.any { it.isLetter() } && login.length in 4..16
}

fun isEmailValid(email: String): Boolean {
    return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    return password.length in 6..12 && hasLetter && hasDigit
}


/*fun registrationUser(email: String, login: String, password: String, userViewModel: UserViewModel, navController: NavHostController) {
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
                            userViewModel.login(userID)

                            navController.navigate("MenuHub")
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
}*/


fun checkLoginInDB(login: String, onEmailReceived: (String?) -> Unit, onError: (Exception) -> Unit) {
    val firebaseRef = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
    firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val userData = ArrayList<User>()
            if (snapshot.exists()) {
                for (items in snapshot.children) {
                    val item = items.getValue(User::class.java)
                    if (item != null && item.login?.contains(login, ignoreCase = false) == true) {
                        userData.add(item)
                    }
                }
                if (userData.isNotEmpty()) {
                    val email = userData.first().email
                    onEmailReceived(email)
                } else {
                    onEmailReceived(null)
                }
            } else {
                onEmailReceived(null)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(error.toException())
        }
    })
}


