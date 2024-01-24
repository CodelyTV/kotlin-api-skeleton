package com.codely.competition.players.domain

import com.codely.competition.clubs.domain.Club
import com.codely.competition.ranking.domain.League

data class Player(
    val id: PlayerId,
    val name: String,
    val club: Club,
    val initialRanking: Ranking,
    val initialLeague: League,
    val promotedToHigherLeagues: Boolean
) {
    companion object {
        fun create(
            id: PlayerId,
            name: String,
            club: String,
            initialRanking: Int,
            league: League,
            promotedToHigherLeagues: Boolean
        ) = Player(id, name, Club(club), initialRanking, league, promotedToHigherLeagues)
    }
}

typealias PlayerId = Long
typealias Ranking = Int
