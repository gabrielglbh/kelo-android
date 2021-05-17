package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.GroupFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Group(
    @DocumentId var id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("currency") val currency: String = ""
) {
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            GroupFields.name to name,
            GroupFields.currency to currency
        )
    }
}