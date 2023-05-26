package com.app.ecom.Remote.Model

data class RegisterProdModel(
    var IDProduct: String? = "",
    var UUIDProduct: String? = "",
    var name: String = "",
    var description: String = "",
    var prodPage: String = "",
    var rate: Int = 0,
    var price: Int = 0,
    var content: String = "",
    var size: String = "",
    var discount: String = "",
    var photoUrl: String? = "",
    var section: String? = "",
    var timestamp: Long = 0

){
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        "",
        "",
        "",
        "",
        "",
        0)

}
