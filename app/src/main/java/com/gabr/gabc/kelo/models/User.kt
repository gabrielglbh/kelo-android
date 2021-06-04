package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.UserFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/**
 * Class that holds the User for managing communications with Firebase
 * */
class User(
    @DocumentId var id: String = "",
    @PropertyName(UserFields.name) val name: String = "",
    @PropertyName(UserFields.points) var points: Int = 0,
    // Added JvmField for Boolean for Kotlin to use the proper setters and getters in the JVM
    @field:JvmField
    @PropertyName(UserFields.isAdmin)
    var isAdmin: Boolean = false,
    @PropertyName(UserFields.messagingToken) var messagingToken: String = ""
) {
    /**
     * Transforms the current [User] into a [Map] to be uploaded to Firebase
     * */
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            UserFields.name to name,
            UserFields.points to points,
            UserFields.isAdmin to isAdmin,
            UserFields.messagingToken to messagingToken
        )
    }

    override fun equals(other: Any?): Boolean {
        val user = other as User
        return id == user.id && name == user.name && points == user.points && isAdmin == user.isAdmin
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + points
        result = 31 * result + isAdmin.hashCode()
        return result
    }
}