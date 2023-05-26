package com.app.ecom.ui.Main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.app.ecom.Navigation.SetupNavHost
import com.app.ecom.ViewModel.*
import com.app.ecom.ui.theme.EComTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val RegisterVM by viewModels<RegisterViewModel> ()
        val LoginVM by viewModels<LoginViewModel>()
        val AddProductVM by viewModels<AddProductViewModel>()
        val DetailsVM by viewModels<DetailsViewModel>()
        val AccountVM by viewModels<AccountViewModel>()
        val WishlistVM by viewModels<WishlistViewModel>()
        val ShoppingCarVM by viewModels<ShoppingCarViewModel>()

        super.onCreate(savedInstanceState)
        setContent {
            EComTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    SetupNavHost(
                        navHostController = navController,
                        registerViewModel = RegisterVM,
                           loginViewModel = LoginVM,
                            context = this,
                        AddProductViewModel = AddProductVM,
                        detailsViewModel = DetailsVM,
                        accountViewModel = AccountVM,
                        wishlistViewModel = WishlistVM,
                        shoppingCarViewModel = ShoppingCarVM
                    )
                }
            }
        }
    }
}

