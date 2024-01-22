package com.codely.competition.players.domain

import com.codely.competition.clubs.domain.Club

interface PlayerRepository {

    suspend fun save(player: Player)
    suspend fun find(criteria: FindPlayerCriteria): Player?
    suspend fun search(criteria: SearchPlayerCriteria): List<Player>
}

sealed interface FindPlayerCriteria {
    class ById(val id: Long): FindPlayerCriteria
    class ByClubAndName(val club: Club, val name: String): FindPlayerCriteria
}

sealed interface SearchPlayerCriteria {
    class ByClub(val club: Club): SearchPlayerCriteria
}
