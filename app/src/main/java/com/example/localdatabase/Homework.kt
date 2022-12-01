package com.example.localdatabase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Homework (
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null
): Parcelable
