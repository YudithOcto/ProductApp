package com.app.productcatalog.util

import java.text.NumberFormat
import java.util.Locale

object StringUtils {
    fun stringToRupiah(input: Double): String {
        return try {
            val localeID = Locale("id", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            formatRupiah.format(input).replace("Rp", "Rp ")
        } catch (e: Exception) {
            "Rp 0"
        }
    }
}