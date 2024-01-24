package com.codely.shared

import com.codely.competition.results.WeeklyResultUpdater
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING


@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.codely"])
@ComponentScan("com.codely")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)

//    val calendarPdfResource = ClassPathResource("/pdf/calendari-3A.pdf")
//    val rankingPdfResource = ClassPathResource("/pdf/ranking-3a-A.pdf")

//    val tempCalendarPdfFile = Files.createTempFile("temp-pdf-calendari-3A", ".pdf").toFile()
//    val tempRankingPdfFile = Files.createTempFile("temp-pdf-ranking-3A", ".pdf").toFile()

//    Files.copy(calendarPdfResource.inputStream, tempCalendarPdfFile.toPath(), REPLACE_EXISTING)
//    Files.copy(rankingPdfResource.inputStream, tempRankingPdfFile.toPath(), REPLACE_EXISTING)

//    processCalendarPDF(tempCalendarPdfFile)
//    processRankingPDF(tempRankingPdfFile)
}





