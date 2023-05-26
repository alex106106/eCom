package com.app.ecom.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AccountViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    private val _userData = MutableStateFlow<RegisterModel?>(null)
    val userDataVM : StateFlow<RegisterModel?> = _userData.asStateFlow()
    fun userData(){
        viewModelScope.launch {
            val userData = getUserData()
            _userData.value = userData
        }
    }
    suspend fun getUserData(): RegisterModel? {
        return try {
            val cu = auth.currentUser
            val ref = database.getReference("users/${cu?.uid}/userData")
            val snapshot = ref.get().await()
            snapshot.getValue(RegisterModel::class.java)
        }catch (e: Exception){
            null
        }
    }















}