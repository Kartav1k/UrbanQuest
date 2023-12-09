package com.example.urbanquest.ui.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urbanquest.data.models.UserData

class RegistrationViewModel: ViewModel() {
    val userLiveData: MutableLiveData<UserData> = MutableLiveData()
    fun registerUser(user: UserData){
        userLiveData.postValue(user)
    }
}