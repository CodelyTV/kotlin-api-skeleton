package com.codely.competition.clubs.domain

import com.codely.competition.ranking.domain.League
import java.util.UUID

data class Club(
    val clubName: ClubName,
    val league: League,
    val id: UUID = UUID.randomUUID()
)

@JvmInline
value class ClubName(val value: String)
