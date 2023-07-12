package com.codely.agenda.mothers

import com.codely.agenda.domain.AvailableHour
import com.codely.agenda.domain.HourType
import com.codely.agenda.domain.MaxCapacity
import com.codely.agenda.domain.Player
import java.util.*
import kotlin.random.Random

object AvailableHourMother {

    fun fullPlayerList(
        id: UUID = UUID.randomUUID(),
        from: Int = Random.nextInt(4, 7),
        to: Int = Random.nextInt(5, 8),
        capacity: MaxCapacity = MaxCapacity(8),
        type: HourType = HourType.values().random(),
        players: List<Player> = fullPlayerList
    ) = listOf(AvailableHour(id, from, to, capacity, type, players))

    private val fullPlayerList = listOf(
        Player("Antonio"),
        Player("Marcel"),
        Player("Tino"),
        Player("Txus"),
        Player("Exposito"),
        Player("Carlos"),
        Player("Lucas"),
        Player("Maria"),
    )
}
