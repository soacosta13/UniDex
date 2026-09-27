package com.example.unidex.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Represents a single university returned by the Hipolabs Universities API.
 * It implements Parcelable so it can travel inside an Intent extra (Activity -> Activity)
 * and inside a Fragment's arguments Bundle (Activity -> Fragment).
 */
data class University(
    val name: String,
    val country: String,

    // The JSON key is "alpha_two_code", but we expose a clean Kotlin name
    @SerializedName("alpha_two_code")
    val countryCode: String?,

    // The JSON key literally contains a hyphen, so it can't be a valid Kotlin identifier
    @SerializedName("state-province")
    val stateProvince: String?,

    val domains: List<String>,

    @SerializedName("web_pages")
    val webPages: List<String>
) : Parcelable {
    // Secondary constructor: rebuilds a University by reading values back out of a Parcel, in the exact same order they were written in writeToParcel().
    constructor(parcel: Parcel) : this(
        name = parcel.readString() ?: "",
        country = parcel.readString() ?: "",
        countryCode = parcel.readString(),
        stateProvince = parcel.readString(),
        domains = parcel.createStringArrayList() ?: emptyList(),
        webPages = parcel.createStringArrayList() ?: emptyList()
    )

    // Writes every property into the Parcel, in the same order the constructor above reads them.
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(country)
        parcel.writeString(countryCode)
        parcel.writeString(stateProvince)
        parcel.writeStringList(domains)
        parcel.writeStringList(webPages)
    }

    // Required by the Parcelable interface; 0 is the standard value unless you're
    // parceling a FileDescriptor, which we are not.
    override fun describeContents(): Int = 0

    // The CREATOR is what Android's framework calls internally to rebuild
    // a University from a Parcel (for example, when it comes back out of an Intent).
    companion object CREATOR : Parcelable.Creator<University> {
        override fun createFromParcel(parcel: Parcel): University {
            return University(parcel)
        }

        override fun newArray(size: Int): Array<University?> {
            return arrayOfNulls(size)
        }
    }
}
