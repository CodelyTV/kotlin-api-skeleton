package com.codely.agenda.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.codely.agenda.application.book.BookAgendaError
import com.codely.agenda.application.book.BookAgendaError.MaxCapacityReached
import com.codely.agenda.application.book.BookAgendaError.PlayerAlreadyBooked
import com.codely.agenda.application.book.BookAgendaError.AvailableHourNotFound
import com.codely.agenda.application.cancel.CancelBookingError
import com.codely.agenda.application.cancel.CancelBookingError.PlayerNotBooked
import com.codely.shared.clock.currentMonth
import com.codely.shared.clock.currentYear
import java.util.UUID
import kotlinx.datetime.Month

data class Agenda(
    val id: UUID,
    val day: Day,
    val month: Month,
    val year: Year,
    val availableHours: List<AvailableHour> = emptyList()
) {

    companion object {
        fun create(id: UUID, day: Day) =
            when (day.dayOfWeek.value) {
                1 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.monday())
                2 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.tuesday())
                3 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.wednesday())
                4 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.thursday())
                5 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.friday())
                6 -> Agenda(id, day, currentMonth(), currentYear(), AvailableHour.saturday())
                7 -> Agenda(id, day, currentMonth(), currentYear(), emptyList())
                else -> TODO()
            }
    }

    fun bookAvailableHour(
        availableHourId: UUID,
        playerName: Player
    ): Either<BookAgendaError, Agenda> {
        val availableHour = availableHours.find { it.id == availableHourId }

        return availableHour?.let { hour ->

            if (playerName in hour.registeredPlayers) {
                return PlayerAlreadyBooked.left()
            }

            if (hour.registeredPlayers.size >= hour.capacity.value) {
                return MaxCapacityReached.left()
            }

            val updatedHour = hour.addPlayer(playerName)
            val updatedHours = availableHours.toMutableList()
                .apply {
                    val index = indexOf(hour)
                    set(index, updatedHour)
                }

            copy(availableHours = updatedHours).right()
        } ?: AvailableHourNotFound.left()
    }

    fun cancelBooking(
        availableHourId: UUID,
        playerName: Player
    ): Either<CancelBookingError, Agenda> {
        val availableHour = availableHours.find { it.id == availableHourId }

        return availableHour?.let { hour ->
            if (playerName !in hour.registeredPlayers) {
                return PlayerNotBooked.left()
            }

            val updatedHour = hour.removePlayer(playerName)
            val updatedHours = availableHours.toMutableList()
                .apply {
                    val index = indexOf(hour)
                    set(index, updatedHour)
                }

            copy(availableHours = updatedHours).right()
        } ?: CancelBookingError.AvailableHourNotFound.left()
    }
}

data class AvailableHour(
    val id: UUID = UUID.randomUUID(),
    val from: Int, // 4
    val to: Int, // 5
    val capacity: MaxCapacity = MaxCapacity(8),
    val type: HourType,
    val registeredPlayers: List<Player> // max size depends on capacity
) {

    fun addPlayer(player: Player): AvailableHour = copy(registeredPlayers = registeredPlayers + player)
    fun removePlayer(player: Player): AvailableHour = copy(registeredPlayers = registeredPlayers - player)

    companion object {
        fun monday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun tuesday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 18, to = 19, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun wednesday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun thursday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun friday() = listOf(
            AvailableHour(from = 16, to = 17, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 17, to = 18, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME)
        )

        fun saturday() = listOf(
            AvailableHour(from = 10, to = 11, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 11, to = 12, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 12, to = 13, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME),
            AvailableHour(from = 13, to = 14, registeredPlayers = emptyList(), type = HourType.MEMBERS_TIME)
        )
    }
}

@JvmInline
value class Player(val value: String)

typealias Year = Int

enum class HourType { TEAM_TRAINING, ADULT_ACADEMY, KIDS_ACADEMY, MEMBERS_TIME }

@JvmInline
value class MaxCapacity(val value: Int = 8)
