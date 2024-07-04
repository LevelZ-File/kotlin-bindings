package xyz.calcugames.levelz

/**
 * Exports a Level to a String.
 * @param level Level to Export
 * @param includeHeaders Whether to include Headers in Export
 * @param includeData Whether to include Data in Export
 * @param lineSeparator Line Separator
 * @return Level Export
 */
fun exportToString(
    level: Level,
    includeHeaders: Boolean = true,
    includeData: Boolean = true,
    lineSeparator: String = "\n"
): String {
    val exporter = LevelExporter.export(level)

    exporter.includeHeaders = includeHeaders
    exporter.includeData = includeData
    exporter.lineSeparator = lineSeparator

    return exporter.writeToString()
}

/**
 * Exports this Level to a String.
 * @param includeHeaders Whether to include Headers in Export
 * @param includeData Whether to include Data in Export
 * @param lineSeparator Line Separator
 * @return Level Export
 */
fun Level.exportToString(
    includeHeaders: Boolean = true,
    includeData: Boolean = true,
    lineSeparator: String = "\n"
): String = exportToString(this, includeHeaders, includeData, lineSeparator)