package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.UserFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/**
 * Class that holds the User for managing communications with Firebase
 * */
class User(
    @DocumentId var id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("points") var points: Int = 0
) {
    /**
     * Transforms the current [User] into a [Map] to be uploaded to Firebase
     * */
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            UserFields.name to name,
            UserFields.points to points
        )
    }
}