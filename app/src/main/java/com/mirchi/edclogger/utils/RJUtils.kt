package com.mirchi.edclogger.utils

import java.text.SimpleDateFormat
import java.util.*

data class TimeSlot(val start: String, val end: String, val rjName: String)

fun getCurrentRJName(): String {
    val now = Calendar.getInstance()
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentTime = sdf.format(now.time)

    val rjTimeSlots = listOf(
        TimeSlot("07:00", "11:00", "MirchiDQ"),
        TimeSlot("11:00", "14:00", "MirchiJithu"),
        TimeSlot("14:00", "17:00", "MirchiShivshankari"),
        TimeSlot("17:00", "21:00", "MirchiJoe & Preethy")
    )

    for (slot in rjTimeSlots) {
        if (isInTimeRange(currentTime, slot.start, slot.end)) return slot.rjName
    }
    return "RJ NightBot"
}

fun isInTimeRange(current: String, start: String, end: String): Boolean {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentTime = sdf.parse(current)
    val startTime = sdf.parse(start)
    val endTime = sdf.parse(end)
    return currentTime in startTime..endTime
}