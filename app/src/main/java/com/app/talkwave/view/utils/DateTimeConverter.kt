package com.app.talkwave.view.utils

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Locale

object DateTimeConverter {
    fun convertDate(date: String, originalFormat: String, targetFormat: String): String {
        val locale = Locale.getDefault()
        val formatter = DateTimeFormatter.ofPattern(originalFormat, locale)
        val localDate = LocalDate.parse(date, formatter)
        return localDate.format(DateTimeFormatter.ofPattern(targetFormat, locale))
    }

    fun convertDateTime(dateTime: String, targetFormat: String): String {
        val locale = Locale.getDefault()
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter(locale)

        val localDateTime = LocalDateTime.parse(dateTime, formatter)
        return localDateTime.format(DateTimeFormatter.ofPattern(targetFormat, locale))
    }

    fun formatChatRoomDateTime(dateTime: String): String {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter(Locale.getDefault())

        val parsedDateTime = LocalDateTime.parse(dateTime, formatter)
        val now = LocalDateTime.now()
        val duration = Duration.between(parsedDateTime, now)

        return when {
            duration.toMinutes() < 1 -> "방금 전"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
            duration.toHours() < 24 -> "${duration.toHours()}시간 전"
            duration.toDays() < 1 -> "어제"
            duration.toDays() < 7 -> "${duration.toDays()}일 전"
            parsedDateTime.year == now.year -> parsedDateTime.format(DateTimeFormatter.ofPattern("MM.dd", Locale.getDefault()))
            else -> parsedDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd", Locale.getDefault()))
        }
    }
}