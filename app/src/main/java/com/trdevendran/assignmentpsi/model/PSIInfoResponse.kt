package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class PSIInfoResponse(
    @field:SerializedName("apiInfo")
    val apiInfo: APIInfo,
    @field:SerializedName("region_metadata")
    val regionMetadata: List<RegionMetaDatum>,
    @field:SerializedName("items")
    val items: List<InfoItem>
)