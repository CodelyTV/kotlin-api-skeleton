package com.codely.competition.players.domain

import com.codely.competition.clubs.domain.Club

data class Player(
    val id: PlayerId,
    val name: String,
    val club: Club,
    val initialRanking: Ranking,
    val promotedToHigherLeagues: Boolean
) {
    companion object {
        fun create(
            id: PlayerId,
            name: String,
            club: String,
            initialRanking: Int,
            promotedToHigherLeagues: Boolean
        ) = Player(id, name, Club(club), initialRanking, promotedToHigherLeagues)
    }
}

typealias PlayerId = Long
typealias Ranking = Int
