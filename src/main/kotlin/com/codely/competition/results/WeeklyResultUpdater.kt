package com.codely.competition.results

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File

object WeeklyResultUpdater {

    fun processCalendarPDF(pdfFile: File) {
        val pdDocument = PDDocument.load(pdfFile)
        val textStripper = PDFTextStripper()
        val text = textStripper.getText(pdDocument)

        val splitList = text.split("\n")

        val leagueStandings =
            splitList.subList(6, 18)
                .map { it.replace(Regex("[0-9]"), "").trim() }

        val weekEndResults = splitList
            .subList(leagueStandings.size + 18, splitList.size)
            .getResultsFrom(leagueStandings)

        pdDocument.close()
    }

    private fun List<String>.getResultsFrom(teams: List<String>): Map<String, List<String>> {
        val result = mutableMapOf<String, List<String>>()

        var currentKey: String? = null
        var currentList: MutableList<String>? = null

        for (line in this) {
            val matchResult = Regex("(\\d*)Jornada Acta").find(line)
            val numberBeforeKeyword = matchResult?.groupValues?.get(1)

            if (numberBeforeKeyword != null) {
                // If a new keyword is found, save the current list to the map
                currentKey?.let { key ->
                    currentList?.let { list ->
                        result[key] = list.toList()
                    }
                }

                // Start a new list for the new keyword
                currentKey = "$numberBeforeKeyword Jornada Acta"
                currentList = mutableListOf()
            } else if (teams.any { it in line }) {
                // Add lines to the current list
                currentList?.add(createFinalLine(line))
            }
        }

        // Save the last list to the map
        currentKey?.let { key ->
            currentList?.let { list ->
                result[key] = list.toList()
            }
        }

        return result
    }

    private fun createFinalLine(line: String): String {
        val x = line.removeRange(0, 8)
        return x.removeRange(x.length - 9, x.length).trim()
    }
}
