package com.app.ecom.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ecom.Remote.Model.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {
    fun registerUser(registerModel: RegisterModel): LiveData<String> {
        val auth = FirebaseAuth.getInstance()
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                auth.createUserWithEmailAndPassword(registerModel.email.toString(),
                    registerModel.pass.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val current = FirebaseAuth.getInstance().currentUser
                            val DBR = FirebaseDatabase.getInstance().getReference("users/"+current?.uid + "/userData/")
                            val registerModel = registerModel
                            registerModel.UUID = current?.uid
                            DBR.setValue(registerModel)
                            result.value = "success"
                        } else {
                            result.value = "error"
                        }
                    }
            }
        }

        return result
    }
}