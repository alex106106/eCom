package com.app.ecom.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class DetailsViewModel : ViewModel() {

    private val _selectedProduct = MutableStateFlow<RegisterProdModel?>(null)
    val selectedProduct: StateFlow<RegisterProdModel?> = _selectedProduct.asStateFlow()
    fun productID(id: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val prod = getCountryById(id)
                _selectedProduct.value = prod
            }
        }
    }

    val products = FirebaseDatabase.getInstance().getReference("products")
    private suspend fun getCountryById(id: String): RegisterProdModel? {
        val dataSnapshot = products.orderByChild("idproduct").equalTo(id).get().await()
        return dataSnapshot.children.firstOrNull()?.let { marker ->
            val UUIDProduct = marker.child("uuidproduct").getValue(String::class.java)
            val name = marker.child("name").getValue(String::class.java)
            val description = marker.child("description").getValue(String::class.java)
            val image = marker.child("photoUrl").getValue(String::class.java)
            val rate = marker.child("rate").getValue(Int::class.java)
            val price = marker.child("price").getValue(Int::class.java)
            val size = marker.child("size").getValue(String::class.java)
            val prodPage = marker.child("prodPage").getValue(String::class.java)
            val section = marker.child("section").getValue(String::class.java)

            RegisterProdModel(
                id,
                UUIDProduct ?: "",
                name ?: "",
                description ?: "",
                prodPage ?: "",
                rate ?: 0,
                price ?: 0,
                "",
                size ?: "",
                "",
                image,
                section ?: "",
                0
            )
        }
    }

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    fun UserData(){
        viewModelScope.launch {
            val userData = getUserData()
            userData?.let {
                _name.value = it.name.toString()
            }
        }
    }
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    suspend fun getUserData(): RegisterModel? {
        return try {
            val cu = auth.currentUser
            val ref = database.getReference("users/${cu?.uid}/userData")
            val snapshot = ref.get().await()
            snapshot.getValue(RegisterModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun addStars(commentId: String, userId: String, stars: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                addStar(commentId, userId, stars)
            }
        }
    }
    fun addStar(productId: String, userId: String, stars: Int): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val commentRef = FirebaseDatabase.getInstance().getReference("rates/$productId/rate/$userId")

        commentRef.setValue(stars).addOnSuccessListener {
            result.value = true
        }.addOnFailureListener {
            result.value = false
        }

        return result
    }
    fun getStars(productId: String): LiveData<Pair<Float, Boolean>> {
        val starsLiveData = MutableLiveData<Pair<Float, Boolean>>()
        val starsRef = FirebaseDatabase.getInstance().getReference("rates/$productId/rate/")

        starsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalStars = 0
                var totalUsers = 0
                var currentUserStar = 0
                for (starSnap in snapshot.children) {
                    val value = starSnap.getValue(Int::class.java)
                    if (value != null) {
                        totalStars += value
                        totalUsers++
                    }
                    if (starSnap.key == FirebaseAuth.getInstance().currentUser?.uid) {
                        currentUserStar = value ?: 0
                    }
                }
                val averageStars = if (totalUsers > 0) totalStars.toFloat() / totalUsers else 0f
                starsLiveData.value = Pair(averageStars, currentUserStar > 0)
                Log.d("getStars", "AverageStars: $averageStars - currentUserStar: $currentUserStar")
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error
            }
        })

        return starsLiveData
    }
    fun wishlist(productId: String, userId: String, registerProductToWishListModel: RegisterProdModel){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                addWishlist(productId, userId, registerProductToWishListModel)
            }
        }
    }
    private fun addWishlist(productId: String, userId: String, registerProductToWishListModel: RegisterProdModel): LiveData<RegisterProdModel>{
        val result = MutableLiveData<RegisterProdModel>()
//        val wishProductRef = FirebaseDatabase.getInstance().getReference("wishlist/$productId/userWishlist/$userId")
        val wishProductRef = FirebaseDatabase.getInstance().getReference("wishlist/$userId/userWishlist/$productId")
        wishProductRef.setValue(registerProductToWishListModel).addOnSuccessListener {
            result.value = registerProductToWishListModel
        }.addOnFailureListener {

        }
        return result
    }
    fun shoppingCarList(productId: String, userId: String, registerProductToShoppingCarModel: RegisterProdModel){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                addShoppingCarList(productId, userId, registerProductToShoppingCarModel)
            }
        }
    }
    private fun addShoppingCarList(productId: String, userId: String, registerProductToWishListModel: RegisterProdModel): LiveData<RegisterProdModel>{
        val result = MutableLiveData<RegisterProdModel>()
//        val wishProductRef = FirebaseDatabase.getInstance().getReference("wishlist/$productId/userWishlist/$userId")
        val wishProductRef = FirebaseDatabase.getInstance().getReference("shoppingCar/$userId/userShoppingCar/$productId")
        wishProductRef.setValue(registerProductToWishListModel).addOnSuccessListener {
            result.value = registerProductToWishListModel
        }.addOnFailureListener {

        }
        return result
    }
}