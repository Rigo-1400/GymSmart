package com.example.anew.firebase

object UserSession {
    var userEmail: String? = null
    var userName: String? = null
    var userPhotoUrl: String? = null

    fun setUserData(email: String?, name: String?, photoUrl: String?) {
        userEmail = email
        userName = name
        userPhotoUrl = photoUrl
    }
}
