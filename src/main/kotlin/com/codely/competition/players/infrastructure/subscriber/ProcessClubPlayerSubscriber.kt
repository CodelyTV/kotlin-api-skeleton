package com.codely.competition.players.infrastructure.subscriber

import com.codely.competition.players.application.create.UpdatePlayerCommand
import com.codely.competition.players.application.create.UpdatePlayersCommandHandler
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

//@Component
class ProcessClubPlayerSubscriber(
    private val updater: UpdatePlayersCommandHandler
) {

//    @EventListener(ApplicationReadyEvent::class)
    fun invoke() {
        val playerPdfResource = ClassPathResource("/pdf/jugadors-3a-A.pdf")
        val tempPlayerPdfFile = Files.createTempFile("temp-pdf-jugadors-3A", ".pdf").toFile()
        Files.copy(playerPdfResource.inputStream, tempPlayerPdfFile.toPath(), REPLACE_EXISTING)
        val textStripper = PDFTextStripper()

        PDDocument.load(tempPlayerPdfFile).use { pdDocument ->
            val text = textStripper.getText(pdDocument).split("\n")
            updater.handle(UpdatePlayerCommand(text))
        }
    }
}
