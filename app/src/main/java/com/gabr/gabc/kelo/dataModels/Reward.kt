package com.gabr.gabc.kelo.dataModels

import com.gabr.gabc.kelo.constants.RewardFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

/**
 * Class that holds the Reward for managing communications with Firebase
 * */
data class Reward(
    @DocumentId var id: String = "",
    @PropertyName(RewardFields.name) var name: String = "",
    @PropertyName(RewardFields.expiration) var expiration: Date? = null,
    @PropertyName(RewardFields.frequency) var frequency: Int = -1,
    @PropertyName(RewardFields.icon) var icon: String = ""
) {
    /**
     * Transforms the current [Reward] into a [Map] to be uploaded to Firebase
     * */
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            RewardFields.name to name,
            RewardFields.expiration to expiration,
            RewardFields.frequency to frequency,
            RewardFields.icon to icon
        )
    }
}