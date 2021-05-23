package com.gabr.gabc.kelo.models

import android.os.Parcel
import android.os.Parcelable
import com.gabr.gabc.kelo.constants.ChoreFields
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

/**
 * Class that holds the Chore for managing communications with Firebase
 *
 * [Chore] extends from [Parcelable] in order to be able to pass it through intents
 * It makes a map in the simple scheme of things
 * */
class Chore(
    @DocumentId var id: String? = "",
    @PropertyName("name") var name: String? = "",
    @PropertyName("icon") var icon: String? = "",
    @PropertyName("assignee") var assignee: String? = "",
    @PropertyName("expiration") var expiration: Date? = Calendar.getInstance().time,
    @PropertyName("points") var points: Int = 10
) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDate(),
        parcel.readInt()
    )

    /**
     * Transforms the current [Chore] into a [Map] to be uploaded to Firebase
     * */
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            ChoreFields.name to name,
            ChoreFields.icon to icon,
            ChoreFields.assignee to assignee,
            ChoreFields.expiration to expiration,
            ChoreFields.points to points
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(assignee)
        parcel.writeDate(expiration)
        parcel.writeInt(points)
    }

    override fun describeContents(): Int { return 0 }

    override fun equals(other: Any?): Boolean {
        val chore = other as Chore
        return id == chore.id && name == chore.name &&
                icon == chore.icon && assignee == chore.assignee &&
                expiration == chore.expiration && points == chore.points
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (assignee?.hashCode() ?: 0)
        result = 31 * result + (expiration?.hashCode() ?: 0)
        result = 31 * result + points
        return result
    }

    /** Parcelable companion object for defining helper functions */
    companion object CREATOR : Parcelable.Creator<Chore> {
        private fun Parcel.writeDate(date: Date?) { writeLong(date?.time ?: -1) }
        private fun Parcel.readDate(): Date? {
            val long = readLong()
            return if (long != 1L) Date(long) else null
        }

        override fun createFromParcel(parcel: Parcel): Chore { return Chore(parcel) }
        override fun newArray(size: Int): Array<Chore?> { return arrayOfNulls(size) }
    }
}