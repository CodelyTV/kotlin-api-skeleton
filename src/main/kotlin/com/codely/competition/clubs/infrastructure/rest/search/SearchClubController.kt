package com.codely.competition.clubs.infrastructure.rest.search

import com.codely.competition.clubs.application.search.SearchClubQuery
import com.codely.competition.clubs.application.search.handle
import com.codely.competition.clubs.domain.Club
import com.codely.competition.clubs.domain.ClubRepository
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchClubController(private val repository: ClubRepository): BaseController() {

    @GetMapping("/clubs")
    fun search(@RequestParam league: String): Response<*> = runBlocking {
        with(repository) {
            handle(SearchClubQuery(league))
                .let { clubs -> Response.status(HttpStatus.OK).body(clubs.toDocument()) }
        }
    }
}

data class ClubDocument(val clubs: List<String>)
internal fun List<Club>.toDocument() = ClubDocument(this.map { it.clubName.value })
