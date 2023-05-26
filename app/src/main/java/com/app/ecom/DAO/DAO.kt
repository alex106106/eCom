package com.app.ecom.DAO

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.ecom.Remote.EcomInterface
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import com.app.ecom.ViewModel.AddProductViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class DAO @Inject constructor(): EcomInterface {

	override fun addProduct(productModel: RegisterProdModel): LiveData<RegisterProdModel> {
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

	override suspend fun getAllProducts(): List<RegisterProdModel> {
		val products = mutableListOf<RegisterProdModel>()
		val productsRef = FirebaseDatabase.getInstance().reference.child("products")
		val dataSnapshot = productsRef.get().await()
		for (comentarioSnapshot in dataSnapshot.children) {
			val comentario = comentarioSnapshot.getValue(RegisterProdModel::class.java)
			products.add(comentario!!)
		}
		return products
	}
	val products = FirebaseDatabase.getInstance().getReference("products")
	override suspend fun getProductsById(id: String): RegisterProdModel {
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
		}!!
	}

	val auth = FirebaseAuth.getInstance()
	val database = FirebaseDatabase.getInstance()
	override suspend fun getUserData(): RegisterModel? {
		return try {
			val cu = auth.currentUser
			val ref = database.getReference("users/${cu?.uid}/userData")
			val snapshot = ref.get().await()
			snapshot.getValue(RegisterModel::class.java)
		} catch (e: Exception) {
			null
		}
	}

	override fun addStars(productId: String, userId: String, stars: Int): LiveData<Boolean> {
		val result = MutableLiveData<Boolean>()
		val commentRef = FirebaseDatabase.getInstance().getReference("rates/$productId/rate/$userId")

		commentRef.setValue(stars).addOnSuccessListener {
			result.value = true
		}.addOnFailureListener {
			result.value = false
		}

		return result	}

	override fun getStars(productId: String): LiveData<Pair<Float, Boolean>> {
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

		return starsLiveData	}

	override fun addWishlist(
		productId: String,
		userId: String,
		registerProductToWishListModel: RegisterProdModel
	): LiveData<RegisterProdModel> {
		val result = MutableLiveData<RegisterProdModel>()
//        val wishProductRef = FirebaseDatabase.getInstance().getReference("wishlist/$productId/userWishlist/$userId")
		val wishProductRef = FirebaseDatabase.getInstance().getReference("wishlist/$userId/userWishlist/$productId")
		wishProductRef.setValue(registerProductToWishListModel).addOnSuccessListener {
			result.value = registerProductToWishListModel
		}.addOnFailureListener {

		}
		return result
	}

	override fun addShoppingCarList(
		productId: String,
		userId: String,
		registerProductToWishListModel: RegisterProdModel
	): LiveData<RegisterProdModel> {
		val result = MutableLiveData<RegisterProdModel>()
//        val wishProductRef = FirebaseDatabase.getInstance().getReference("wishlist/$productId/userWishlist/$userId")
		val wishProductRef = FirebaseDatabase.getInstance().getReference("shoppingCar/$userId/userShoppingCar/$productId")
		wishProductRef.setValue(registerProductToWishListModel).addOnSuccessListener {
			result.value = registerProductToWishListModel
		}.addOnFailureListener {

		}
		return result
	}

	override suspend fun getAllWishlistProducts(userId: String): List<RegisterProdModel> {
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