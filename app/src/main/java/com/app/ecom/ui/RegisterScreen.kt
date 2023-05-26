package com.app.ecom.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.ecom.Remote.Model.RegisterModel
import com.app.ecom.Remote.Model.RegisterProdModel
import com.app.ecom.ViewModel.AddProductViewModel
import com.app.ecom.ViewModel.RegisterViewModel
import com.app.ecom.ViewModel.WishlistViewModel
import java.util.*

@Composable
fun RegisterScreen(
    addProductViewModel: AddProductViewModel,
    registerViewModel: RegisterViewModel,
    navController: NavController,
    wishlistViewModel: WishlistViewModel) {
    Scaffold(
        content = { RegisterScreen2(registerViewModel = registerViewModel)},
        bottomBar = { NavigationButton(
            navController = navController,
            addProductViewModel = addProductViewModel,
            wishlistViewModel = wishlistViewModel) }
    )
}

@Composable
fun RegisterScreen2(registerViewModel: RegisterViewModel) {
    var name by remember { mutableStateOf("") }
    var emailUser by remember { mutableStateOf("") }
    var passUser by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    val cont = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.Gray
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
                val pickImageLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()){ result ->
                    if (result.resultCode == Activity.RESULT_OK){
                        val uri = result.data?.data
                        selectedImageUri.value = uri
                    }
                }
                val imageBitmap = selectedImageUri.value?.let { uri ->
                    val bitmap = getBitmapFromUri(uri, cont)
                    bitmap?.asImageBitmap()
                }
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    value = emailUser,
                    onValueChange = { emailUser = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    value = passUser,
                    onValueChange = { passUser = it },
                    label = { Text("Password") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    label = { Text("Confirm Password") }
                )


                Button(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = {
                        pickImageLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
                    }) {
                    Text(text = "Foto")
                }
                Button(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = {
                        // Verificar si se ha seleccionado una imagen
                        if (selectedImageUri.value != null) {
                            val imageName = UUID.randomUUID().toString()
                            uploadImageToFirebase(selectedImageUri.value!!, imageName) { imageUrl ->
                                registerViewModel.registerUser(
                                    registerModel = RegisterModel(
                                        email = emailUser,
                                        pass = passUser,
                                        UUID = "",
                                        name = name,
                                        profile_photo = imageUrl
                                    )
                                )
                            }
                        } else {
                            // No se ha seleccionado una imagen, mostrar un mensaje de error o realizar la acción correspondiente
                        }
                    }
                ) {
                    Text("Register")
                }
                imageBitmap?.let { bitmap ->
                    Image(bitmap, contentDescription = "S")
                }
            }
        }
    }


}