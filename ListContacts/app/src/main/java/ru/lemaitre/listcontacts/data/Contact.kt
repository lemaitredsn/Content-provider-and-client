package ru.lemaitre.listcontacts.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val email: List<String>
) : Parcelable