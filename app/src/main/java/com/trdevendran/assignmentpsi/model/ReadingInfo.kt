package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class ReadingInfo(
    @field:SerializedName("west")
    val west: Int,
    @field:SerializedName("national")
    val national: Int,
    @field:SerializedName("east")
    val east: Int,
    @field:SerializedName("central")
    val central: Int,
    @SerializedName("south")
    val south: Int,
    @field:SerializedName("north")
    val north: Int
)