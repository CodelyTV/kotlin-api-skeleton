package com.codely.competition.clubs.application.create

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository

class ClubsCreator(private val repository: ClubRepository) {

    suspend operator fun invoke(names: List<String>) {
        names.forEach { repository.save(Club(it)) }
    }
}
