package com.codely.competition.players.infrastructure.subscriber

import com.codely.competition.players.application.create.UpdatePlayerCommand
import com.codely.competition.players.application.create.UpdatePlayersCommandHandler
import com.codely.competition.ranking.domain.League
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
class ProcessClubPlayerSubscriber(
    private val updater: UpdatePlayersCommandHandler,
    private val configuration: CompetitionConfig
) {
    private val textStripper = PDFTextStripper()

//    @EventListener(ApplicationReadyEvent::class)
    fun invoke() = runBlocking {
        val (preferente, primera, segundaA, segundaB, terceraA, terceraB) = configuration
        val ligas = listOf(preferente, primera, segundaA, segundaB, terceraA, terceraB)

        ligas.map { liga ->
            liga.players.values
                .map { URL(it) }
                .map { launch { processURLContent(it, liga.name) }.join() }
        }
    }

    private suspend fun processURLContent(url: URL, league: String) {
        PDDocument.load(url.openStream()).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdatePlayerCommand(text, league))
        }
    }
}
