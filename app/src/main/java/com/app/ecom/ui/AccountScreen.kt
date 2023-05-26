package com.app.ecom.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.app.ecom.ViewModel.AccountViewModel
import com.app.ecom.ViewModel.AddProductViewModel
import com.app.ecom.ViewModel.RegisterViewModel
import com.app.ecom.ViewModel.WishlistViewModel

@Composable
fun AccountScreen(
    addProductViewModel: AddProductViewModel,
    accountViewModel: AccountViewModel,
    navController: NavController,
    wishlistViewModel: WishlistViewModel) {
    Scaffold(
        content = { Account(accountViewModel = accountViewModel) },
        bottomBar = { NavigationButton(
            addProductViewModel,
            navController = navController,
            wishlistViewModel = wishlistViewModel) }
    )
}

@Composable
fun Account(accountViewModel: AccountViewModel) {
    val userData by accountViewModel.userDataVM.collectAsState()
    val painter = rememberImagePainter(userData?.profile_photo)
    accountViewModel.userData()

    Text(
        text = userData?.name ?: "",
        fontWeight = FontWeight.Light,
        color = Color.Black,
        modifier = Modifier.fillMaxWidth()
    )
    var isExpanded by remember { mutableStateOf(false) }

    val showDialog = remember { mutableStateOf(false) }

    fun openDialog() {
        showDialog.value = true
    }

    fun closeDialog() {
        showDialog.value = false
    }
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        item {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                // El diálogo
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { closeDialog() },
                        confirmButton = {
                            if (isExpanded){

                                Image(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            isExpanded = false
                                            closeDialog()
                                        }
                                        .size(350.dp),
                                    alignment = Alignment.Center,
                                    contentDescription = "Profile Image",
                                    painter = painter
                                )

                            }
                        }
                    )
                }

                Card(
                    elevation = 7.dp,
                    shape = RoundedCornerShape(7.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column() {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            Box(modifier = Modifier
                                .padding(15.dp)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(
                                            if (scrollState.firstVisibleItemIndex == 0) {
                                                200.dp - (scrollState.firstVisibleItemScrollOffset.toFloat().dp / 2)
                                            } else {
                                                60.dp
                                            }
                                        )
                                        .clip(CircleShape)
                                        .border(
                                            width = 4.dp,
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            openDialog()
                                            isExpanded = true
                                        }
                                    ,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Profile Image",
                                    painter = painter
                                )
                            }
                        }
//                        val show by feedViewModel.showData.observeAsState()

//                        SHOW DATA


                        Text(text = userData?.name ?: "",
                            fontSize = 40.sp,
                            modifier = Modifier
                                .padding(8.dp)
                        )
//                        Text(text = description,
//                            fontSize = 15.sp,
//                            modifier = Modifier
//                                .padding(8.dp)
//                        )

                    }
                }

                Card(
                    elevation = 7.dp,
                    shape = RoundedCornerShape(7.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 7.dp, bottom = 7.dp, start = 5.dp, end = 5.dp)
                ) {
//                    Column() {
//                        Text(text = "Details",
//                            fontFamily = FontFamily(Font(R.font.signikabold)),
//                            fontSize = 20.sp,
//                            modifier = Modifier
//                                .padding(8.dp))
//                        Row(verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .padding(8.dp)) {
//                            Icon(painter = painterResource(id = R.drawable.cel), contentDescription = "")
//                            Text(text = bd,
//                                fontFamily = FontFamily(Font(R.font.signikabold)),
//                                fontSize = 15.sp,
//                                modifier = Modifier
//                                    .padding(8.dp))
//                        }
//
//                        Row(verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .padding(8.dp)) {
//                            Icon(painter = painterResource(id = R.drawable.location), contentDescription = "")
//                            Text(text = from,
//                                fontFamily = FontFamily(Font(R.font.signikabold)),
//                                fontSize = 15.sp,
//                                modifier = Modifier
//                                    .padding(8.dp))
//                        }
//                        Row(verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .padding(8.dp)) {
//                            Icon(painter = painterResource(id = R.drawable.email), contentDescription = "")
//                            Text(text = email,
//                                fontFamily = FontFamily(Font(R.font.signikabold)),
//                                fontSize = 15.sp,
//                                modifier = Modifier
//                                    .padding(8.dp))
//                        }
//                    }
                }


            }
        }

    }
}
