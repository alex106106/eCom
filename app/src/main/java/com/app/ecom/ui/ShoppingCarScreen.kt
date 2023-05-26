package com.app.ecom.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.app.ecom.Util.PaymentsUtil
import com.app.ecom.ViewModel.AddProductViewModel
import com.app.ecom.ViewModel.ShoppingCarViewModel
import com.app.ecom.ViewModel.WishlistViewModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


@Composable
fun ShoppingCarScreen(
    userId: String,
    shoppingCarViewModel: ShoppingCarViewModel,
    navController: NavController,
    addProductViewModel: AddProductViewModel,
    wishlistViewModel: WishlistViewModel) {
    Scaffold(
        content = { ShoppingCar(userId, shoppingCarViewModel) },
        bottomBar = { NavigationButton(
            navController = navController,
            addProductViewModel = addProductViewModel,
            wishlistViewModel = wishlistViewModel) }
    )
}
@Composable
fun ShoppingCar(userId: String, shoppingCarViewModel: ShoppingCarViewModel) {
    val wish by shoppingCarViewModel.shoppingCar.collectAsState()
    val context = LocalContext.current
    val paymentsClient = remember { PaymentsUtil.createPaymentsClient(context as Activity) }
    val garmentList = remember { mutableStateOf(JsonArray()) }
    val selectedGarment = remember { mutableStateOf<JsonObject?>(null) }
    val googlePayAvailable = remember { mutableStateOf(false) }
    val paymentResult = remember { mutableStateOf<String?>(null) }
    shoppingCarViewModel.getAllWishlist(userId)
    Box {
        LazyColumn {
            wish.forEach { products ->
                // display each comment
                item {
                    Card(
                        modifier = Modifier
                            .padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp),
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color.White,
                    ) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .align(Alignment.CenterVertically)
                                    .weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(2.dp)
                                ) {
                                    val painter = rememberImagePainter(products.photoUrl)
                                    Card(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .width(175.dp)
                                            .height(115.dp)
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .size(200.dp),
                                            alignment = Alignment.Center,
                                            contentDescription = "Profile Image",
                                            painter = painter
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        val text = products.name
                                        Text(
                                            text = text,
                                            color = Color.Black,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Row() {
                                            Text(
                                                text = "Price ",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Light
                                            )

                                            Text(
                                                text = "$${products.price}",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Light
                                            )
                                        }
                                        Button(onClick = {

                                        }) {
                                            Text(text = "Pay")
                                        }
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }

    }


}

