package com.app.ecom.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.ecom.Util.Constants.Screens.ACCOUNT_SCREEN
import com.app.ecom.Util.Constants.Screens.DETAILS_SCREEN
import com.app.ecom.Util.Constants.Screens.KEY_USER_ID
import com.app.ecom.Util.Constants.Screens.LOGIN_SCREEN
import com.app.ecom.Util.Constants.Screens.MAIN_SCREEN
import com.app.ecom.Util.Constants.Screens.NAV
import com.app.ecom.Util.Constants.Screens.REGISTER_PRODUCT_SCREEN
import com.app.ecom.Util.Constants.Screens.REGISTER_SCREEN
import com.app.ecom.Util.Constants.Screens.SHOPPING_CAR_SCREEN
import com.app.ecom.Util.Constants.Screens.WISH_LIST_SCREEN
import com.app.ecom.ViewModel.*
import com.app.ecom.ui.*

sealed class Screens(val route: String){
    object Main: Screens(route = MAIN_SCREEN)
    object Login: Screens(route = LOGIN_SCREEN)
    object RegisterProduct: Screens(route = REGISTER_PRODUCT_SCREEN)
    object Details: Screens(route = DETAILS_SCREEN)
    object Register: Screens(route = REGISTER_SCREEN)
    object Nav: Screens(route = NAV)
    object Account: Screens(route = ACCOUNT_SCREEN)
    object ShoppingCar: Screens(route = SHOPPING_CAR_SCREEN)
    object WishList: Screens(route = WISH_LIST_SCREEN)

}
@Composable
fun SetupNavHost(
    navHostController: NavHostController,
//    viewModel: mainViewModel,
    registerViewModel: RegisterViewModel,
    loginViewModel: LoginViewModel,
    AddProductViewModel: AddProductViewModel,
    detailsViewModel: DetailsViewModel,
    accountViewModel: AccountViewModel,
    wishlistViewModel: WishlistViewModel,
    shoppingCarViewModel: ShoppingCarViewModel,
    context: Context
){
    NavHost(
        navController = navHostController,
        startDestination = Screens.Login.route){
        composable(route = Screens.RegisterProduct.route){
            RegisterProdScreen(
                registerViewModel = AddProductViewModel)
        }
        composable(route = Screens.Login.route){
            loginScreen(
                loginViewModel = loginViewModel,
                navController = navHostController,
                context = context)
        }
        composable(route = Screens.Register.route){
            RegisterScreen(
                addProductViewModel = AddProductViewModel,
                registerViewModel = registerViewModel,
                navController = navHostController,
                wishlistViewModel = wishlistViewModel)
        }
        composable(route = Screens.Main.route){
            MainScreen(
                addProductViewModel = AddProductViewModel,
                navController = navHostController,
                wishlistViewModel = wishlistViewModel)
        }
        composable(route = Screens.Account.route){
            AccountScreen(
                accountViewModel = accountViewModel,
                navController = navHostController,
                addProductViewModel = AddProductViewModel,
                wishlistViewModel = wishlistViewModel)
        }
        composable(route = Screens.ShoppingCar.route + "/{$KEY_USER_ID}"){ backStackEntry ->
            ShoppingCarScreen(
                userId = backStackEntry.arguments?.getString(KEY_USER_ID) ?: "",
                shoppingCarViewModel = shoppingCarViewModel,
                navController = navHostController,
                addProductViewModel = AddProductViewModel,
                wishlistViewModel = wishlistViewModel)
        }
        composable(route = Screens.WishList.route + "/{$KEY_USER_ID}"){ backStackEntry ->
            WishListScreen(
                userId = backStackEntry.arguments?.getString(KEY_USER_ID) ?: "", wishlistViewModel,
                navController = navHostController,
                addProductViewModel = AddProductViewModel)
        }
        composable(route = Screens.Details.route + "/{$KEY_USER_ID}"){ backStackEntry ->
            DetailsScreen(
                id = backStackEntry.arguments?.getString(KEY_USER_ID) ?: "",
                detailsViewModel = detailsViewModel)
        }

    }
}