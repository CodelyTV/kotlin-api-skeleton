package com.codely.competition.clubs.domain

import com.codely.competition.ranking.domain.League

interface ClubRepository {
    suspend fun save(club: Club)
    suspend fun search(criteria: SearchClubCriteria): List<Club>
    suspend fun exists(criteria: ClubExistsCriteria): Boolean
}

sealed interface ClubExistsCriteria {
    class ByNameAndLeague(val clubName: ClubName, val league: League): ClubExistsCriteria
}

sealed interface SearchClubCriteria {
    data object All: SearchClubCriteria
    class ByLeague(val league: League): SearchClubCriteria
}
