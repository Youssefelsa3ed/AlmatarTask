package com.youssefelsa3ed.almatarchallenge.model

import android.os.Parcel
import android.os.Parcelable

data class Doc(
    val authorName: List<String?>? = null,
    val authorKey: List<String?>? = null,
    val isbn: List<String?>? = null,
    val title: String? = null,
    val key: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(authorName)
        parcel.writeStringList(authorKey)
        parcel.writeStringList(isbn)
        parcel.writeString(title)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doc> {
        override fun createFromParcel(parcel: Parcel): Doc {
            return Doc(parcel)
        }

        override fun newArray(size: Int): Array<Doc?> {
            return arrayOfNulls(size)
        }
    }
}
