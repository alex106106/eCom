package com.app.ecom.Repository

import androidx.lifecycle.LiveData
import com.app.ecom.DAO.DAO
import com.app.ecom.Remote.EcomInterface
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import javax.inject.Inject

class EcomRepository @Inject constructor(val DAO: DAO){
	fun addProduct(productModel: RegisterProdModel): LiveData<RegisterProdModel> {
		return DAO.addProduct(productModel)
	}

	suspend fun getAllProducts(): List<RegisterProdModel> {
		return DAO.getAllProducts()
	}

	suspend fun getProductsById(id: String): RegisterProdModel {
		return DAO.getProductsById(id)
	}

	suspend fun getUserData(): RegisterModel? {
		return DAO.getUserData()
	}

	fun addStars(productId: String, userId: String, stars: Int): LiveData<Boolean> {
		return DAO.addStars(productId, userId, stars)
	}

	fun getStars(productId: String): LiveData<Pair<Float, Boolean>> {
		return DAO.getStars(productId)
	}

	fun addWishlist(
		productId: String,
		userId: String,
		registerProductToWishListModel: RegisterProdModel
	): LiveData<RegisterProdModel> {
		return DAO.addWishlist(productId,userId,registerProductToWishListModel)
	}

	fun addShoppingCarList(
		productId: String,
		userId: String,
		registerProductToWishListModel: RegisterProdModel
	): LiveData<RegisterProdModel> {
		return DAO.addShoppingCarList(productId, userId, registerProductToWishListModel)
	}

	suspend fun getAllWishlistProducts(userId: String): List<RegisterProdModel> {
		return DAO.getAllWishlistProducts(userId)
	}

}