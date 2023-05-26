package com.app.ecom.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.app.ecom.Navigation.Screens

import com.app.ecom.ViewModel.AddProductViewModel
import com.app.ecom.ViewModel.WishlistViewModel
import com.google.firebase.annotations.concurrent.Background
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException


@Composable
fun MainScreen(
    addProductViewModel: AddProductViewModel,
    navController: NavController,
    wishlistViewModel: WishlistViewModel) {
    Scaffold(
        topBar = { TopAppBar()},
        content = { Home(
            addProductViewModel = addProductViewModel,
            navController = navController)},
        bottomBar = { NavigationButton(
            addProductViewModel,
            navController = navController,
            wishlistViewModel = wishlistViewModel)}
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(addProductViewModel: AddProductViewModel, navController: NavController) {
   val prod by addProductViewModel.productList.collectAsState()
   var selectedProd by remember { mutableStateOf("") }
    addProductViewModel.getAllProd()

   Box {
      LazyColumn {
         prod.forEach { products ->
            // display each comment
            item {
                Card(
                    onClick = {
                        selectedProd = products.IDProduct!!
                            true
                    },
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
                                .align(CenterVertically)
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
                                        .align(CenterVertically)
                                ) {
                                    val text = products.name
                                    limitText(
                                        text = text, maxLength = 40)
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
    LaunchedEffect(selectedProd){
        if (selectedProd.isNotEmpty()){
            navController.navigate(Screens.Details.route + "/$selectedProd")
        }
    }
}
@Composable
fun limitText(text: String, maxLength: Int){
    val limitedText = if (text.length > maxLength){
        text.take(maxLength) + "..."
    }else{
        text
    }
    Text(
        text = limitedText,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.fillMaxWidth(1f))

}

@Composable
fun NavigationButton(
    addProductViewModel: AddProductViewModel,
    navController: NavController,
    wishlistViewModel: WishlistViewModel) {
//    val user by addProductViewModel.productList.collectAsState()
//    var selectedProd by remember { mutableStateOf("") }
    val product by addProductViewModel.productList.collectAsState()
    val wish by wishlistViewModel.wishlist.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser?.uid

    BottomNavigation(
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { navController.navigate(Screens.Main.route) }
            ) {
                Icon(painter = painterResource(id = com.app.ecom.R.drawable.home), contentDescription = "")
            }
            IconButton(
                onClick = { navController.navigate(Screens.Account.route) }
            ) {
                Icon(painter = painterResource(id = com.app.ecom.R.drawable.account), contentDescription = "")
            }
            IconButton(
                onClick = { navController.navigate(Screens.ShoppingCar.route + "/$user") }
            ) {
                Row() {
                    Icon(painter = painterResource(id = com.app.ecom.R.drawable.card), contentDescription = "")
                    Text(product.size.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Top))
                }
            }
            IconButton(
                onClick = { navController.navigate(Screens.WishList.route + "/$user") }
            ) {
                Row() {
                    Icon(painter = painterResource(id = com.app.ecom.R.drawable.wish), contentDescription = "")
                    Text(wish.size.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Top))
                }
            }
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut()
            }) {
                Icon(painter = painterResource(id = com.app.ecom.R.drawable.logout), contentDescription = "")
            }
        }
    }

}

@Composable
fun TopAppBar() {
    var searchText by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = CenterVertically
            ) {
                if (isSearchExpanded) {
                    AnimatedVisibility(
                        visible = isSearchExpanded,
                        enter = fadeIn() + slideInHorizontally(),
                        exit = fadeOut() + slideOutHorizontally()
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text(text = "Buscar") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.Black,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }
                } else {
                    Text(text = "eCom", color = Color.Black, fontSize = 16.sp)
                }
            }
        },
        actions = {
            IconButton(onClick = { isSearchExpanded = !isSearchExpanded }) {
                Icon(
                    imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
        },
        modifier = Modifier.height(52.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}




