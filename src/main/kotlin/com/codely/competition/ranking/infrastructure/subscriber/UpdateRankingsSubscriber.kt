package com.codely.competition.ranking.infrastructure.subscriber

import com.codely.competition.ranking.application.update.UpdateRankingCommand
import com.codely.competition.ranking.application.update.UpdateRankingCommandHandler
import kotlinx.coroutines.runBlocking
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

@Component
class UpdateRankingsSubscriber(
    private val updater: UpdateRankingCommandHandler
) {

    @EventListener(ApplicationReadyEvent::class)
    fun invoke() = runBlocking {
        val rankingPdfResource = ClassPathResource("/pdf/ranking-3a-A.pdf")
        val tempRankingPdfFile = Files.createTempFile("temp-pdf-ranking-3A", ".pdf").toFile()
        Files.copy(rankingPdfResource.inputStream, tempRankingPdfFile.toPath(), REPLACE_EXISTING)
        val textStripper = PDFTextStripper()

        PDDocument.load(tempRankingPdfFile).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdateRankingCommand(text))
        }
    }
}
