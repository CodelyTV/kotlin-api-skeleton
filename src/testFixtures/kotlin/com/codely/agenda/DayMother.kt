package com.codely.agenda

import com.codely.agenda.domain.Day
import java.time.DayOfWeek
import kotlin.random.Random

object DayMother {

    fun monday(number: Int = Random.nextInt(1, 32)) = Day(number, DayOfWeek.MONDAY)
    fun tuesday(number: Int = Random.nextInt(1, 32)) = Day(number, DayOfWeek.TUESDAY)
    fun wednesday(number: Int = Random.nextInt(1, 32)) = Day(number, DayOfWeek.WEDNESDAY)
    fun thursday(number: Int = Random.nextInt(1, 32)) = Day(number, DayOfWeek.THURSDAY)
    fun friday(number: Int = Random.nextInt(1, 32)) = Day(number, DayOfWeek.FRIDAY)
    fun saturday(number: Int = Random.nextInt(1, 32)) = Day(number, DayOfWeek.SATURDAY)
}
