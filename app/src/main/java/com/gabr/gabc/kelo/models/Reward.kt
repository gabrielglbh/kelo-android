package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.RewardFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

/**
 * Class that holds the Reward for managing communications with Firebase
 * */
class Reward(
    @DocumentId var id: String = "",
    @PropertyName(RewardFields.name) val name: String = "",
    @PropertyName(RewardFields.frequency) val frequency: Date = Calendar.getInstance().time,
    @PropertyName(RewardFields.icon) val icon: String = ""
) {
    /**
     * Transforms the current [Reward] into a [Map] to be uploaded to Firebase
     * */
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            RewardFields.name to name,
            RewardFields.frequency to frequency,
            RewardFields.icon to icon
        )
    }
}