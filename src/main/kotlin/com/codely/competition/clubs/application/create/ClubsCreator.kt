package com.codely.competition.clubs.application.create

import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ClubsCreator(private val repository: ClubRepository) {

    suspend operator fun invoke(names: List<String>) = coroutineScope {
        names
            .map { Club(it) }
            .filter { !repository.exists(it) }
            .forEach { launch { repository.save(it) }.join() }
    }
}
