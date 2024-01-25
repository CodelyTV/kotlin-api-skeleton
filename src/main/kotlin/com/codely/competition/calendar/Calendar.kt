package com.codely.competition.calendar

import com.codely.competition.clubs.domain.ClubName
import java.time.ZonedDateTime
import java.util.*

data class Calendar(
    val id: UUID,
    val name: String,
    val matches: List<Match>
)

data class Match(
    val localClub: ClubName,
    val visitorClub: ClubName,
    val result: Result?,
    val dateTime: ZonedDateTime
)

data class Result(
    val winner: ClubName,
    val winnerGames: Int,
    val loserGames: Int
)
