package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.UserFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

class User(
    @DocumentId var id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("points") var points: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            UserFields.name to name,
            UserFields.points to points
        )
    }
}