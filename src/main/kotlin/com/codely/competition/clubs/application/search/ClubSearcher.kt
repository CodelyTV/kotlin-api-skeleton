package com.codely.competition.clubs.application.search

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.SearchClubCriteria.ByLeague
import com.codely.competition.ranking.domain.League

context(ClubRepository)
suspend fun searchClub(league: League): List<Club> = search(ByLeague(league))
