package com.gabr.gabc.kelo.dataModels

import android.os.Parcel
import android.os.Parcelable
import com.gabr.gabc.kelo.constants.RewardFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

/**
 * Class that holds the Reward for managing communications with Firebase
 * */
data class Reward(
    @DocumentId var id: String? = "",
    @PropertyName(RewardFields.name) var name: String? = "",
    @PropertyName(RewardFields.expiration) var expiration: Date? = null,
    @PropertyName(RewardFields.frequency) var frequency: Int = -1,
    @PropertyName(RewardFields.icon) var icon: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDate(),
        parcel.readInt(),
        parcel.readString()
    )

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeDate(expiration)
        parcel.writeInt(frequency)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reward> {
        private fun Parcel.writeDate(date: Date?) { writeLong(date?.time ?: -1) }
        private fun Parcel.readDate(): Date? {
            val long = readLong()
            return if (long != 1L) Date(long) else null
        }

        override fun createFromParcel(parcel: Parcel): Reward {
            return Reward(parcel)
        }

        override fun newArray(size: Int): Array<Reward?> {
            return arrayOfNulls(size)
        }
    }
}