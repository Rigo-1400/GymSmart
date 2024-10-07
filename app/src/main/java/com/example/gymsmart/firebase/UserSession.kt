package com.example.gymsmart.firebase

/**
 * User session
 *
 * @constructor Create empty User session
 */
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
