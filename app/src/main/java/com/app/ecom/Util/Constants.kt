package com.app.ecom.Util

import com.google.android.gms.wallet.WalletConstants
import com.google.firebase.auth.FirebaseAuth

class Constants {
    object Screens{
        const val LOGIN_SCREEN = "login_screen"
        const val MAIN_SCREEN = "main_screen"
        const val REGISTER_PRODUCT_SCREEN = "register_product_screen"
        const val DETAILS_SCREEN = "details_screen"
        const val REGISTER_SCREEN = "register_screen"
        const val NAV = "nav_screen"
        const val ACCOUNT_SCREEN = "account_screen"
        const val SHOPPING_CAR_SCREEN = "shopping_car_screen"
        const val WISH_LIST_SCREEN = "wish_list_screen"
        const val KEY_USER_ID = "com.app.ecom"
    }
    object const{
        val AUTH = FirebaseAuth.getInstance()
    }
    object Constants{
        const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

        /**
         * The allowed networks to be requested from the API. If the user has cards from networks not
         * specified here in their account, these will not be offered for them to choose in the popup.
         *
         * @value #SUPPORTED_NETWORKS
         */
        val SUPPORTED_NETWORKS = listOf(
            "AMEX",
            "DISCOVER",
            "JCB",
            "MASTERCARD",
            "VISA")

        /**
         * The Google Pay API may return cards on file on Google.com (PAN_ONLY) and/or a device token on
         * an Android device authenticated with a 3-D Secure cryptogram (CRYPTOGRAM_3DS).
         *
         * @value #SUPPORTED_METHODS
         */
        val SUPPORTED_METHODS = listOf(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS")

        /**
         * Required by the API, but not visible to the user.
         *
         * @value #COUNTRY_CODE Your local country
         */
        const val COUNTRY_CODE = "US"

        /**
         * Required by the API, but not visible to the user.
         *
         * @value #CURRENCY_CODE Your local currency
         */
        const val CURRENCY_CODE = "USD"

        /**
         * Supported countries for shipping (use ISO 3166-1 alpha-2 country codes). Relevant only when
         * requesting a shipping address.
         *
         * @value #SHIPPING_SUPPORTED_COUNTRIES
         */
        val SHIPPING_SUPPORTED_COUNTRIES = listOf("US", "GB")

        /**
         * The name of your payment processor/gateway. Please refer to their documentation for more
         * information.
         *
         * @value #PAYMENT_GATEWAY_TOKENIZATION_NAME
         */
        const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "example"

        /**
         * Custom parameters required by the processor/gateway.
         * In many cases, your processor / gateway will only require a gatewayMerchantId.
         * Please refer to your processor's documentation for more information. The number of parameters
         * required and their names vary depending on the processor.
         *
         * @value #PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS
         */
        val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
            "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
            "gatewayMerchantId" to "exampleGatewayMerchantId"
        )

        /**
         * Only used for `DIRECT` tokenization. Can be removed when using `PAYMENT_GATEWAY`
         * tokenization.
         *
         * @value #DIRECT_TOKENIZATION_PUBLIC_KEY
         */
        const val DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME"

        /**
         * Parameters required for `DIRECT` tokenization.
         * Only used for `DIRECT` tokenization. Can be removed when using `PAYMENT_GATEWAY`
         * tokenization.
         *
         * @value #DIRECT_TOKENIZATION_PARAMETERS
         */
        val DIRECT_TOKENIZATION_PARAMETERS = mapOf(
            "protocolVersion" to "ECv1",
            "publicKey" to DIRECT_TOKENIZATION_PUBLIC_KEY
        )
    }
}