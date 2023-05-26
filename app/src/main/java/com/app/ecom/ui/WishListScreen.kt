package com.app.ecom.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.app.ecom.ViewModel.AddProductViewModel
import com.app.ecom.ViewModel.RegisterViewModel
import com.app.ecom.ViewModel.WishlistViewModel

@Composable
fun WishListScreen(userId: String, wishlistViewModel: WishlistViewModel, navController: NavController, addProductViewModel: AddProductViewModel) {
    Scaffold(
        content = { WishList(userId, wishlistViewModel) },
        bottomBar = { NavigationButton(
            navController = navController,
            addProductViewModel = addProductViewModel,
            wishlistViewModel = wishlistViewModel) }
    )
}
@Composable
fun WishList(userId: String, wishlistViewModel: WishlistViewModel) {
    val wish by wishlistViewModel.wishlist.collectAsState()
    wishlistViewModel.getAllWishlist(userId)
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