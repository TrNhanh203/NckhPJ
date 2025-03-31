package com.example.facilitiesmanagementpj.data.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatTime(millis: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(millis))
}

fun formatDuration(durationMillis: Long): String {
    val minutes = durationMillis / 60_000
    val seconds = (durationMillis % 60_000) / 1000
    return "${minutes} phút ${seconds} giây"
}
