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

class WishlistViewModel : ViewModel() {

    private val _wish = MutableStateFlow(emptyList<RegisterProdModel>())
    val wishlist: StateFlow<List<RegisterProdModel>> = _wish.asStateFlow()

    fun getAllWishlist(userId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val wish = getAllWishlistProducts(userId)
                _wish.value = wish
            }
        }
    }
    suspend fun getAllWishlistProducts(userId: String): List<RegisterProdModel>{
        val result = mutableListOf<RegisterProdModel>()
        val wishlistRef = FirebaseDatabase.getInstance().getReference("wishlist/$userId/userWishlist")
        val dataSnapshot = wishlistRef.get().await()
        for (wishSnapshot in dataSnapshot.children){
            val wish = wishSnapshot.getValue(RegisterProdModel::class.java)
            result.add(wish!!)
        }
        return result
    }
}