package com.basculasmagris.richiger.utils

import android.content.Context
import com.basculasmagris.richiger.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Helper {
    companion object {

        fun getFormattedWeight(weight: Double, context: Context) : String {

            if  (weight > 0.0) {
                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.DOWN
                val formattedWeight = df.format(weight)
                return "$formattedWeight ${context.getString(R.string.lb_weight_unit)}"
            } else {
                return context.getString(R.string.lbl_empty)
            }
        }

        fun getFormattedWeightKg(weight: Double, context: Context) : String {

            if  (weight > 0.0) {
                val df = DecimalFormat("#.###")
                df.roundingMode = RoundingMode.DOWN
                val formattedWeight = df.format(weight)
                return "$formattedWeight ${context.getString(R.string.lb_weight_unit_kg)}"
            } else {
                return context.getString(R.string.lbl_empty)
            }
        }

        fun getNumberWithDecimals(value: Double, decimals: Int) : String {

            var pattern = "#"
            if (decimals > 0){
                pattern += "."
            }

            for (index in 1..decimals){
                pattern += "#"
            }

            return if  (value > 0.0) {
                val df = DecimalFormat(pattern)
                df.roundingMode = RoundingMode.DOWN
                df.format(value)
            } else {
                "0.00"
            }
        }

        fun getAppDateFromString(date: String) : LocalDateTime {
            var localDateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
            return LocalDateTime.parse(date, localDateFormat)
        }

        fun getApiDateFromString(date: String) : LocalDateTime{
            var remoteDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'")
            return LocalDateTime.parse(date, remoteDateFormat)
        }
    }
}