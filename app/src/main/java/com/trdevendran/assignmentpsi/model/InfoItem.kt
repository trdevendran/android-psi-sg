package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class InfoItem(
    @field:SerializedName("timestamp")
    val timestamp: String,
    @field:SerializedName("updateTimestamp")
    val updateTimestamp: String,
    @field:SerializedName("readings")
    val readings: Readings
)