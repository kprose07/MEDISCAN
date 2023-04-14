package com.example.mediscan.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NarrowDownSearch(
    val title: String,
    val data: String
) : Parcelable