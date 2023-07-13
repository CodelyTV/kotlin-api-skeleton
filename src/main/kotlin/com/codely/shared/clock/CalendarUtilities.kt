package com.codely.shared.clock

import com.codely.agenda.domain.Day
import com.codely.agenda.domain.Week
import com.codely.agenda.domain.Year
import java.util.Calendar
import java.util.TimeZone as JavaTimeZone
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun currentMonth(): Month = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).month
fun currentDay(): Day = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).let { Day(it.dayOfMonth, it.dayOfWeek) }
fun currentYear(): Year = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).year
fun currentWeek(): Week = Calendar.getInstance(JavaTimeZone.getTimeZone("UTC")).get(Calendar.WEEK_OF_YEAR)
