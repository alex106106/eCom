package com.app.ecom.Remote.Model

data class RegisterModel (
    var email: String? = "",
    var pass: String? = "",
    var name: String? = "",
    var UUID: String? = "",
    var profile_photo: String? = ""

){
    constructor() : this("", "", "", "")
}