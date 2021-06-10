package com.gabr.gabc.kelo.dataModels

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.constants.Constants
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

    /** Defines the selected frequencies for the periodicity of a Reward */
    enum class Frequencies {
        NO_FREQUENCY, WEEKLY, EVERY_TWO_WEEKS, MONTHLY, EVERY_TWO_MONTHS, ANNUALLY;

        companion object {
            /**
             * Function that depending on the selected periodicity of the reward, returns a [Date] representing
             * said periodicity starting 'today'
             *
             * @param mode: selected periodicity
             * @return [Date] object representing the periodicity
             * */
            fun getDateFromMode(mode: Frequencies): Date? {
                val date = Calendar.getInstance()
                when (mode) {
                    NO_FREQUENCY -> return null
                    WEEKLY -> date.add(Calendar.WEEK_OF_YEAR, 1)
                    EVERY_TWO_WEEKS -> date.add(Calendar.WEEK_OF_YEAR, 2)
                    MONTHLY -> date.add(Calendar.MONTH, 1)
                    EVERY_TWO_MONTHS -> date.add(Calendar.MONTH, 2)
                    ANNUALLY -> date.add(Calendar.YEAR, 1)
                }
                return date.time
            }

            /**
             * Depending of the mode of the selected periodicity, returns an specific string for updating
             * the UI accordingly
             *
             * @param context: current context
             * @param mode: selected periodicity
             * @return string defined for each available periodicity
             * */
            fun getStringFromMode(context: Context, mode: Int): String {
                return when (mode) {
                    NO_FREQUENCY.ordinal -> context.getString(R.string.rewards_no_frequency)
                    WEEKLY.ordinal -> context.getString(R.string.rewards_weekly)
                    EVERY_TWO_WEEKS.ordinal -> context.getString(R.string.rewards_two_weeks)
                    MONTHLY.ordinal -> context.getString(R.string.rewards_monthly)
                    EVERY_TWO_MONTHS.ordinal -> context.getString(R.string.rewards_two_months)
                    ANNUALLY.ordinal -> context.getString(R.string.rewards_annually)
                    else -> context.getString(R.string.settings_reward_button_placeholder)
                }
            }
        }
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