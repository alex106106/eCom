package com.app.ecom.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.ecom.Remote.Model.RegisterProdModel
import com.app.ecom.ViewModel.AddProductViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*



@Composable
fun RegisterProdScreen(registerViewModel: AddProductViewModel) {
    val addProduct by registerViewModel.addProduct.observeAsState()

    var name by remember { mutableStateOf("Niko Junior") }
    var emailUser by remember { mutableStateOf("alexisgalindo106@gmail.com") }
    var passUser by remember { mutableStateOf("juniorniko106") }
    var confirmPass by remember { mutableStateOf("juniorniko106") }
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
                    if (result.resultCode == RESULT_OK){
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
                                registerViewModel.addProduct(
                                    productModel = RegisterProdModel(
                                        "",
                                        "",
                                        "Samsung, Smartphone, Desbloqueado, Android 10.0, 4G, Capacidad de alamacenamiento 128 GB, Bluetooth, USB, WIFI, Negro, 6.5 Pulgadas, Tecnología inalámbrica GSM LTE",
                                        "El Samsung Galaxy A51 cuenta con una pantalla Súper AMOLED de 6.5 pulgadas con una resolución de 1080 x 2400 píxeles, con marcos casi invisibles que permite una visualización más inmersiva. Responde rápidamente cada toque en su pantalla funciona instantáneamente gracias a su procesador Octa-core 2.3 Ghz y su memoria RAM de 6GB. Con su almacenamiento de 128GB no volverás a preocuparte pues te proporciona suficiente espacio para tu música, videos y fotos. El A51 incorpora una cuádruple cámara trasera con múltiples posibilidades pues sus Fotografías son nítidas y brillantes de gran amplitud gracias a su lente ultra gran angular o detalles cercanos con su lente macro. Su sistema fotográfico lo complementa una cámara principal de 48 MP para día y noche, 12 MP de ultra gran angular con apertura, 5MP de cámara macro para primerísimos planos con excelente captación de textura y efectos de desenfoque proporcionados por su cámara de profundidad de 5 MP. Posee una batería de 4000 mAh con carga rápida de 15W, tu smartphone aguantará todo el día. Su lector de huella integrado en pantalla ahora posee más superficie para una mayor velocidad y la protección de tu Samsung está asegurada gracias al Samsung KNOX.",
                                     "https://www.apple.com/mx/store?afid=p238%7CsLHHgzTlu-dc_mtid_1870765e38482_pcrid_648481411831_pgrid_15377145411_pntwk_g_pchan__pexid__&cid=aos-mx-kwgo-brand--slid---product-",
                                        5,
                                        5472,
                                        "",
                                        "6.5 pulgadas",
                                        "",
                                        imageUrl,
                                        "Tecnologia"
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
fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    return try {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun uploadImageToFirebase(uri: Uri, imageName: String, callback: (String) -> Unit) {
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("images/$imageName.jpg")

    val uploadTask = imagesRef.putFile(uri)
    uploadTask.addOnSuccessListener { taskSnapshot ->
        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
            val imageUrl = downloadUrl.toString()
            callback(imageUrl)
        }
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }
}

