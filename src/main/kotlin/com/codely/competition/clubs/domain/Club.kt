package com.codely.competition.clubs.domain

import com.codely.competition.ranking.domain.League

data class Club(
    val clubName: ClubName,
    val league: League
)

@JvmInline
value class ClubName(val value: String)
