package com.codely.shared.clock

import com.codely.agenda.domain.Week
import com.codely.agenda.domain.Year
import java.time.LocalDate
import java.util.Calendar
import kotlinx.datetime.Clock
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.TimeZone as JavaTimeZone

fun currentMonth(): Month = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).month
fun currentYear(): Year = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).year
fun currentWeek(): Week = Calendar.getInstance(JavaTimeZone.getTimeZone("UTC")).get(Calendar.WEEK_OF_YEAR)

fun week(localDate: LocalDate) = Calendar.getInstance()
