package com.app.productcatalog.utils

import com.app.productcatalog.util.StringUtils
import org.junit.Assert
import org.junit.Test

class StringUtilsTest {

    @Test
    fun testStringToRupiah_withValidInput() {
        val input = 116797.936
        val expectedOutput = "Rp 116.797,94"
        val actualOutput = StringUtils.stringToRupiah(input)
        Assert.assertEquals(expectedOutput, actualOutput)
    }
}