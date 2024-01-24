package com.codely.competition.ranking.infrastructure.rest.search

import com.codely.competition.ranking.application.search.SearchLeagueRankingQuery
import com.codely.competition.ranking.application.search.handle
import com.codely.competition.ranking.domain.LeagueRankingRepository
import com.codely.competition.ranking.infrastructure.rest.error.LeagueRankingServerErrors.LEAGUE_RANKING_DOES_NOT_EXIST
import com.codely.shared.cors.BaseController
import com.codely.shared.response.Response
import com.codely.shared.response.withBody
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets.UTF_8

@RestController
class SearchRankingController(private val repository: LeagueRankingRepository): BaseController() {

    @GetMapping("/rankings")
    fun search(@RequestParam league: String, @RequestParam club: String): Response<*> = runBlocking {
        with(repository) {
            handle(SearchLeagueRankingQuery(league, club.decodedParameter()))
                ?.let { ranking -> Response.status(OK).body(ranking.toDocument()) }
                ?: Response.status(NOT_FOUND).withBody(LEAGUE_RANKING_DOES_NOT_EXIST)
        }
    }

    private fun String.decodedParameter(): String = URLDecoder.decode(this, UTF_8)
}
