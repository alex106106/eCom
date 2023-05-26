package com.app.ecom.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddViewModel : ViewModel() {
    fun registerProd(register: RegisterProdModel): LiveData<String> {
        val result = MutableLiveData<String>()
        val timestamp = System.currentTimeMillis()
        val uuid = UUID.randomUUID().toString()
        val DBR = FirebaseDatabase.getInstance().getReference("products/$uuid")
        val feedModels = register
//        feedModels.UUID = uuid
        DBR.setValue(feedModels).addOnSuccessListener {
            result.value = feedModels.toString()
        }.addOnFailureListener {
            // Manejar el fallo en caso de error
        }
        return result
    }

}