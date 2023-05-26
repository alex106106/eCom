package com.app.ecom.Remote

import androidx.lifecycle.LiveData
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import com.app.ecom.ViewModel.AddProductViewModel

interface EcomInterface {
	//	Products
	fun addProduct(productModel: RegisterProdModel): LiveData<RegisterProdModel>
	suspend fun getAllProducts(): List<RegisterProdModel>
//	Details
	suspend fun getProductsById(id: String): RegisterProdModel
	suspend fun getUserData(): RegisterModel?
	fun addStars(productId: String, userId: String, stars: Int): LiveData<Boolean>
	fun getStars(productId: String): LiveData<Pair<Float, Boolean>>
	fun addWishlist(productId: String, userId: String, registerProductToWishListModel: RegisterProdModel): LiveData<RegisterProdModel>
//	Shopping car
	fun addShoppingCarList(productId: String, userId: String, registerProductToWishListModel: RegisterProdModel): LiveData<RegisterProdModel>
	suspend fun getAllWishlistProducts(userId: String): List<RegisterProdModel>

}