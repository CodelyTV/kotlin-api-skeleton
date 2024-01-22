package com.codely.competition.clubs.application.create

import com.codely.competition.clubs.domain.ClubRepository
import org.springframework.stereotype.Component

@Component
class CreateClubsCommandHandler(repository: ClubRepository) {

    private val createClubs = ClubsCreator(repository)

    suspend fun handle(command: CreateClubsCommand) {
        createClubs(command.names)
    }
}

data class CreateClubsCommand(val names: List<String>)
