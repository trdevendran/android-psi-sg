package com.trdevendran.assignmentpsi

import com.trdevendran.assignmentpsi.model.*
import com.trdevendran.assignmentpsi.util.Constants
import com.trdevendran.assignmentpsi.view.MainActivity
import org.junit.Assert
import org.junit.Test

/**
 * This class contains the test cases for the functional/unit test of [MainActivity]
 */

class MainActivityTest {

    private val activity = MainActivity()

    // Test data to ensure the positive case
    private val psiInfo1 = PSIInfoResponse(
        APIInfo("healthy"),
        arrayListOf(RegionMetaDatum("east", LabelLocation(1.35735, 103.7))),
        arrayListOf(
            InfoItem(
                "2019-11-10T22:00:00+08:00",
                "2019-10-11T22:03:52+08:00",
                Readings(
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    COEightHourMaxReading(0.13, 0.14, 0.11, 0.14, 0.10, 0.12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12),
                    ReadingInfo(13, 14, 11, 14, 10, 12)
                )
            )
        )
    )

    // Test data to ensure the negative case
    private val psiInfo2 = PSIInfoResponse(
        APIInfo("healthy"),
        arrayListOf(RegionMetaDatum("east", LabelLocation(1.35735, 103.7))),
        arrayListOf()
    )

    // Test data to ensure the negative case
    private val psiInfo3 = PSIInfoResponse(
        APIInfo("healthy"),
        arrayListOf(),
        arrayListOf()
    )

    // Constant string for the information of reading
    private val baseReadingContent =
        "o3_sub_index: %s\\npm10_twenty_four_hourly: %s\\npm10_sub_index: %s\\nco_sub_index: %s\\npm25_twenty_four_hourly: %s\\nso2_sub_index: %s\\nco_eight_hour_max: %s\\nno2_one_hour_max: %s\\nso2_twenty_four_hourly: %s\\npm25_sub_index: %s\\npsi_twenty_four_hourly: %s\\no3_eight_hour_max: %s\\n"

    // Constant string for the fake result east region
    private val fakeResult =
        "o3_sub_index: 11\\npm10_twenty_four_hourly: 11\\npm10_sub_index: 11\\nco_sub_index: 11\\npm25_twenty_four_hourly: 11\\nso2_sub_index: 11\\nco_eight_hour_max: 0.11\\nno2_one_hour_max: 11\\nso2_twenty_four_hourly: 11\\npm25_sub_index: 11\\npsi_twenty_four_hourly: 11\\no3_eight_hour_max: 11\\n"

    @Test
    fun check_EmptyString_Validation_ReadingInfo() {
        Assert.assertTrue(activity.getReadingInfo("", psiInfo1, "") == "")
        assert(activity.getReadingInfo("", psiInfo1, "") == "")
        assert(activity.getReadingInfo("", psiInfo1, baseReadingContent) == "")
    }

    @Test
    fun check_EmptyList_Validation_ReadingInfo() {
        assert(activity.getReadingInfo(Constants.APIKeys.EAST, psiInfo3, "") == "")
        assert(activity.getReadingInfo(Constants.APIKeys.EAST, psiInfo2, baseReadingContent) == "")
        assert(activity.getReadingInfo(Constants.APIKeys.EAST, psiInfo1, baseReadingContent) != "")
    }

    @Test
    fun check_ReadingInfo_Content() {
        Assert.assertTrue(
            activity.getReadingInfo(
                Constants.APIKeys.EAST,
                psiInfo1,
                baseReadingContent
            ) != ""
        )
        Assert.assertFalse(
            activity.getReadingInfo(
                Constants.APIKeys.EAST,
                psiInfo1,
                baseReadingContent
            ) == ""
        )
        Assert.assertTrue(
            activity.getReadingInfo(
                Constants.APIKeys.EAST,
                psiInfo1,
                baseReadingContent
            ) == fakeResult
        )
    }
}