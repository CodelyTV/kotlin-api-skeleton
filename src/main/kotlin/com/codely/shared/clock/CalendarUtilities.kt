package com.codely.shared.clock

import com.codely.agenda.domain.Year
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentMonth(): Month = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).month
fun currentDay(): DayOfWeek = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).dayOfWeek
fun currentYear(): Year = Clock.System.now().toLocalDateTime(TimeZone.of("UTC")).year
