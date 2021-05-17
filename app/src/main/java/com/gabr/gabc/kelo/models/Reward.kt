package com.gabr.gabc.kelo.models

import com.gabr.gabc.kelo.constants.RewardFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

class Reward(
    @DocumentId var id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("frequency") val frequency: Date = Calendar.getInstance().time,
    @PropertyName("icon") val icon: String = ""
) {
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            RewardFields.name to name,
            RewardFields.frequency to frequency,
            RewardFields.icon to icon
        )
    }
}