package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class RegionMetaDatum(
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("label_location")
    val labelLocation: LabelLocation
)