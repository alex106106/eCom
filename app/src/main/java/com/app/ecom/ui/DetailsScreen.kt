package com.app.ecom.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import com.app.ecom.Remote.Model.RegisterProdModel
import com.app.ecom.R
import com.app.ecom.ViewModel.DetailsViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun DetailsScreen(id: String, detailsViewModel: DetailsViewModel) {
    Scaffold(
        topBar = { TopAppBarDetails(detailsViewModel = detailsViewModel)},
        content = { Details(id = id, detailsViewModel = detailsViewModel)}
    )
}

@Composable
fun Details(id: String, detailsViewModel: DetailsViewModel) {
    val selectedProd by detailsViewModel.selectedProduct.collectAsState(null)
    val name by detailsViewModel.name.collectAsState()
    val text by remember { mutableStateOf("") }
    var stars by remember { mutableStateOf(0) }
    detailsViewModel.UserData()
    LaunchedEffect(id){
        detailsViewModel.productID(id)
    }

    LazyColumn(
        modifier = Modifier.padding(10.dp)
    ) {
        item {
//            Text(
//                text = name,
//                fontWeight = FontWeight.Light,
//                color = Color.White,
//                modifier = Modifier.fillMaxWidth()
//            )

            val painter = rememberImagePainter(selectedProd?.photoUrl)
            val scale = remember { mutableStateOf(1f) }
            val maxScale = 3f
            val minScale = 0.5f
            val isZoomed = remember { mutableStateOf(false) }
            val modifier = if (isZoomed.value){
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->
                            scale.value *= zoom
                            scale.value = scale.value.coerceIn(minScale, maxScale)
                        }
                        detectDragGestures { change, dragAmount ->
                            scale.value = dragAmount.x
                            scale.value = dragAmount.y
                        }
                    }
                    .scale(scale.value)
                    .clipToBounds()
            }else{
                Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clickable {
                        isZoomed.value = true
                    }
            }


            Image(
                painter = painter,
                contentDescription = "foto",
                modifier = modifier,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${selectedProd?.price.toString()}",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                val starsInfo by detailsViewModel.getStars(productId = selectedProd?.IDProduct ?: "").observeAsState(initial = null)

                starsInfo?.let { (globalAverageStars, currentUserStar) ->
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        val fullStars = globalAverageStars.toInt()
                        val remainingStars = 5 - fullStars

                        repeat(fullStars) {
                            Icon(
                                painter = painterResource(id = R.drawable.star),
                                contentDescription = "",
                                tint = colorResource(id = R.color.yellowIn),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (globalAverageStars - fullStars >= 0.5) {
                            Icon(
                                painter = painterResource(id = R.drawable.halfstar),
                                contentDescription = "",
                                tint = colorResource(id = R.color.yellowIn),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        repeat(remainingStars) {
                            Icon(
                                painter = painterResource(id = R.drawable.emptystar),
                                contentDescription = "",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
            /*  AGREGAR ESTRELLAS  */
//            Text(
//                text = "Seleccione el número de estrellas:",
//                style = TextStyle(fontSize = 18.sp)
//            )
//            Text(
//                text = "Estrellas seleccionadas: $stars",
//                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//            Row(
//                modifier = Modifier.padding(top = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                for (i in 1..5) {
//                    IconButton(
//                        onClick = { stars = i },
//                        modifier = Modifier.size(32.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = if (i <= stars) R.drawable.wish else R.drawable.home),
//                            contentDescription = ""
//                        )
//                    }
//                }
//            }
//            Button(
//                onClick = { detailsViewModel.addStars(selectedProd?.IDProduct.toString(), userId = FirebaseAuth.getInstance().currentUser?.uid ?: "", stars) },
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text(text = "Agregar estrellas")
//            }


            Text(
                text = "Product",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
            Card(
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp)
                    .fillMaxWidth(),
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.White,
            ) {
                Text(
                    text = buildAnnotatedString {
                        val characteristics = selectedProd?.name?.split(",") ?: emptyList()

                        characteristics.forEachIndexed { index, characteristic ->
                            val bulletPoint = "• " // Viñeta
                            val line = "$bulletPoint$characteristic\n"
                            val startIndex = length
                            append(line)

                            // Aplicar formato a la viñeta
                            addStyle(
                                style = SpanStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                ),
                                start = startIndex,
                                end = startIndex + bulletPoint.length
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

            }
            Text(
                text = "Description",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
            selectedProd?.let { product ->
                ExpandableCard(selectedProd = product)
            }
            Text(
                text = "Summary details",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
            Card(
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp)
                    .fillMaxWidth(),
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.White,
            ) {
                Text(
                    text = selectedProd?.size ?: "",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                }
            }
        }
    }



@Composable
fun HyperlinkText(text: String, onClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append(text)
            addStringAnnotation("URL", "", 0, text.length)
        }
    }

    Text(
        text = annotatedString,
        modifier = Modifier.clickable { onClick() }
    )
}
@Composable
fun ExpandableCard(selectedProd: RegisterProdModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(top = 7.dp, bottom = 7.dp, start = 14.dp, end = 14.dp)
            .fillMaxWidth(1f)
            .clickable { expanded = !expanded },
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = selectedProd.name,
                fontWeight = FontWeight.Light,
                color = Color.Black,
            )
            if (expanded) {
                Text(
                    text = selectedProd.description,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TopAppBarDetails(detailsViewModel: DetailsViewModel) {
    val selectedProd by detailsViewModel.selectedProduct.collectAsState(null)

    TopAppBar(
        title = { null }, // Remueve el título existente
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Alineación de espaciado entre los elementos
            ) {
                IconButton(
                    modifier = Modifier.weight(1f), // Peso del primer IconButton
                    onClick = {
                        detailsViewModel.shoppingCarList(
                            selectedProd?.IDProduct ?: "",
                            FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            registerProductToShoppingCarModel = RegisterProdModel(
                                selectedProd?.IDProduct ?: "",
                                selectedProd?.UUIDProduct ?: "",
                                selectedProd?.name ?: "",
                                selectedProd?.description ?: "",
                                "",
                                0,
                                selectedProd?.price ?: 0,
                                selectedProd?.content ?: "",
                                selectedProd?.size ?: "",
                                "",
                                selectedProd?.photoUrl ?: "",
                                selectedProd?.section ?: "",
                                0
                            )
                            )
                    }
                ) {
                    Icon(painterResource(
                        id = R.drawable.addshopingcar),
                        contentDescription = "",
                        tint = Color.Black)
                }
                IconButton(
                    modifier = Modifier.weight(1f), // Peso del segundo IconButton
                    onClick = {
                        detailsViewModel.wishlist(
                            selectedProd?.IDProduct ?: "",
                            FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            registerProductToWishListModel = RegisterProdModel(
                                selectedProd?.IDProduct ?: "",
                                selectedProd?.UUIDProduct ?: "",
                                selectedProd?.name ?: "",
                                selectedProd?.description ?: "",
                                "",
                                0,
                                selectedProd?.price ?: 0,
                                selectedProd?.content ?: "",
                                selectedProd?.size ?: "",
                                "",
                                selectedProd?.photoUrl ?: "",
                                selectedProd?.section ?: "",
                                0
                            )
                        )
                    }
                ) {
                    Icon(painterResource(
                        id = R.drawable.wish),
                        contentDescription = "",
                        tint = Color.Black)
                }
            }

        },
        modifier = Modifier
            .height(52.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}





















