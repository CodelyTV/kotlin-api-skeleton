package com.codely.competition.clubs.application.search

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.ranking.domain.League

context(ClubRepository)
suspend fun handle(query: SearchClubQuery): List<Club> =
    searchClub(league = League.valueOf(query.league))


data class SearchClubQuery(val league: String)
