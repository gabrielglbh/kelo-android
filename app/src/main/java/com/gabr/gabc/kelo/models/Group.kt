package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.GroupFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/** Class that holds the Group for managing communications with Firebase */
data class Group(
    @DocumentId var id: String = "",
    @PropertyName(GroupFields.name) val name: String = "",
    @PropertyName(GroupFields.currency) val currency: String = ""
) {
    /**
     * Transforms the current [Group] into a [Map] to be uploaded to Firebase
     * */
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            GroupFields.name to name,
            GroupFields.currency to currency
        )
    }
}