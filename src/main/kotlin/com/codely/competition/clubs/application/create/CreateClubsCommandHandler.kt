package com.codely.competition.clubs.application.create

import com.codely.competition.clubs.domain.ClubRepository
import com.codely.competition.clubs.domain.ClubName
import com.codely.competition.ranking.domain.League
import org.springframework.stereotype.Component

@Component
class CreateClubsCommandHandler(repository: ClubRepository) {

    private val createClubs = ClubsCreator(repository)

    suspend fun handle(command: CreateClubsCommand) {
        createClubs(command.names.map { ClubName(it) }, League.valueOf(command.league))
    }
}

data class CreateClubsCommand(val names: List<String>, val league: String)
