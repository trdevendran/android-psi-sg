package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class APIInfo(
    @field:SerializedName("status")
    val status: String
)