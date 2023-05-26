package com.app.ecom.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ecom.Remote.Model.RegisterProdModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ShoppingCarViewModel : ViewModel() {
    private val _shoppingCar = MutableStateFlow(emptyList<RegisterProdModel>())
    val shoppingCar: StateFlow<List<RegisterProdModel>> = _shoppingCar.asStateFlow()

    fun getAllWishlist(userId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val wish = getAllShoppingCarList(userId)
                _shoppingCar.value = wish
            }
        }
    }
    suspend fun getAllShoppingCarList(userId: String): List<RegisterProdModel>{
        val result = mutableListOf<RegisterProdModel>()
        val shoppingCarRef = FirebaseDatabase.getInstance().getReference("shoppingCar/$userId/userShoppingCar")
        val dataSnapshot = shoppingCarRef.get().await()
        for (shoppingCarSnapshot in dataSnapshot.children){
            val wish = shoppingCarSnapshot.getValue(RegisterProdModel::class.java)
            result.add(wish!!)
        }
        return result
    }

}