package com.trdevendran.assignmentpsi.model

import com.google.gson.annotations.SerializedName

data class Readings(
    @field:SerializedName("o3_sub_index")
    val o3SubIndex: ReadingInfo,
    @field:SerializedName("pm10_twenty_four_hourly")
    val pm10TwentyFourHourly: ReadingInfo,
    @field:SerializedName("pm10_sub_index")
    val pm10SubIndex: ReadingInfo,
    @field:SerializedName("co_sub_index")
    val coSubIndex: ReadingInfo,
    @field:SerializedName("pm25_twenty_four_hourly")
    val pm25TwentyFourHourly: ReadingInfo,
    @field:SerializedName("so2_sub_index")
    val so2SubIndex: ReadingInfo,
    @field:SerializedName("co_eight_hour_max")
    val coEightHourMax: COEightHourMaxReading,
    @field:SerializedName("no2_one_hour_max")
    val no2OneHourMax: ReadingInfo,
    @field:SerializedName("so2_twenty_four_hourly")
    val so2TwentyFourHourly: ReadingInfo,
    @field:SerializedName("pm25_sub_index")
    val pm25SubIndex: ReadingInfo,
    @field:SerializedName("psi_twenty_four_hourly")
    val psiTwentyFourHourly: ReadingInfo,
    @field:SerializedName("o3_eight_hour_max")
    val o3EightHourMax: ReadingInfo
)