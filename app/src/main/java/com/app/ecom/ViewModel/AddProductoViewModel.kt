package com.app.ecom.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ecom.Remote.Model.RegisterProdModel
import com.app.ecom.Repository.EcomRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


class AddProductViewModel : ViewModel() {

    private val _product = MutableStateFlow(emptyList<RegisterProdModel>())
    val productList: StateFlow<kotlin.collections.List<RegisterProdModel>> = _product.asStateFlow()

    private val _addProduct = MutableLiveData<RegisterProdModel>()
    val addProduct: LiveData<RegisterProdModel> = _addProduct

    fun addProduct2(productModel: RegisterProdModel){
        viewModelScope.launch {
            val addProduct = addProduct(productModel)
            _addProduct.value = addProduct.value
        }
    }
    fun addProduct(productModel: RegisterProdModel): LiveData<RegisterProdModel> {
        val result = MutableLiveData<RegisterProdModel>()
        val uuid = UUID.randomUUID().toString()
        productModel.IDProduct = uuid
        productModel.UUIDProduct = FirebaseAuth.getInstance().currentUser?.uid
        val commentsRef = FirebaseDatabase.getInstance().getReference("products/$uuid")
        commentsRef.setValue(productModel).addOnSuccessListener {
            result.value = productModel
        }.addOnFailureListener {
            // Manejar el error
        }
        return result
    }

    fun getAllProd(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val prod = getAllProducts()
                _product.value = prod
            }
        }
    }
    private suspend fun getAllProducts(): List<RegisterProdModel> {
        val products = mutableListOf<RegisterProdModel>()
        val productsRef = FirebaseDatabase.getInstance().reference.child("products")
        val dataSnapshot = productsRef.get().await()
        for (comentarioSnapshot in dataSnapshot.children) {
            val comentario = comentarioSnapshot.getValue(RegisterProdModel::class.java)
            products.add(comentario!!)
        }
        return products
    }
}