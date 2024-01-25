package com.codely.competition.clubs.application.create

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubExistsCriteria.ByNameAndLeague
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.ranking.domain.League
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ClubsCreator(private val repository: ClubRepository) {

    suspend operator fun invoke(clubNames: List<ClubName>, league: League) = coroutineScope {
        clubNames
            .forEach { clubName ->
                launch {
                    if (repository.exists(ByNameAndLeague(clubName, league))) Unit.also { println("Club $clubName already exists in $league")  }
                    else repository.save(Club(clubName, league)).also { println("Persisting $clubName in $league")  }
                }.join()
            }
    }
}
