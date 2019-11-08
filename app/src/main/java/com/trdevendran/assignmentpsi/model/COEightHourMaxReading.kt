package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class COEightHourMaxReading(
    @field:SerializedName("west")
    val west: Double,
    @field:SerializedName("national")
    val national: Double,
    @field:SerializedName("east")
    val east: Double,
    @field:SerializedName("central")
    val central: Double,
    @SerializedName("south")
    val south: Double,
    @field:SerializedName("north")
    val north: Double
)