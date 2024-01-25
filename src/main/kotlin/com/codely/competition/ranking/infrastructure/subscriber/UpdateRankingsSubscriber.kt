package com.codely.competition.ranking.infrastructure.subscriber

import com.codely.competition.ranking.application.update.UpdateRankingCommand
import com.codely.competition.ranking.application.update.UpdateRankingCommandHandler
import com.codely.competition.ranking.domain.League.*
import com.codely.shared.config.CompetitionConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.net.URL

@Component
class UpdateRankingsSubscriber(
    private val updater: UpdateRankingCommandHandler,
    private val configuration: CompetitionConfig
) {

    private val textStripper = PDFTextStripper()

    @EventListener(ApplicationReadyEvent::class)
    fun invoke() = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB) = configuration

        val urls = mapOf(
            PREFERENT to URL(preferente.ranking),
            PRIMERA to URL(primera.ranking),
            SEGUNDA_A to URL(segundaA.ranking),
            SEGUNDA_B to URL(segundaB.ranking),
            TERCERA_A to URL(terceraA.ranking),
            TERCERA_B to URL(terceraB.ranking)
        )

        urls.forEach { ( league, url) ->
            launch { processURLContent(url, league.name) }.join()
        }

    }

    private suspend fun processURLContent(url: URL, league: String) {
        PDDocument.load(url.openStream()).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdateRankingCommand(text, league))
        }
    }
}
