package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class LabelLocation(
    @field:SerializedName("latitude")
    val latitude: Double,
    @field:SerializedName("longitude")
    val longitude: Double
)