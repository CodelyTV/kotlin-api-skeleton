package com.codely.agenda

import com.codely.agenda.domain.Agenda
import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.domain.Day
import com.codely.agenda.domain.Year
import java.util.UUID
import kotlin.random.Random
import kotlinx.datetime.Month

object AgendaMother {

    fun random(
        id: UUID = UUID.randomUUID(),
        day: Day = DayMother.monday(),
        month: Month = Month.values().random(),
        year: Year = Random.nextInt(2015, 2025),
        availableHours: List<AvailableHour> = emptyList()
    ) = Agenda(id, day, month, year, availableHours)

    fun monday(day: Day = DayMother.monday()) = random(day = day, availableHours = AvailableHour.monday())
    fun tuesday(day: Day = DayMother.tuesday()) = random(day = day, availableHours = AvailableHour.tuesday())
    fun wednesday(day: Day = DayMother.wednesday()) = random(day = day, availableHours = AvailableHour.wednesday())
    fun thursday(day: Day = DayMother.thursday()) = random(day = day, availableHours = AvailableHour.thursday())
    fun friday(day: Day = DayMother.friday()) = random(day = day, availableHours = AvailableHour.friday())
    fun saturday(day: Day = DayMother.saturday()) = random(day = day, availableHours = AvailableHour.saturday())

    fun fullyBooked() = random(availableHours = AvailableHourMother.fullPlayerList())
}
