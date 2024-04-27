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
    val builder= StringBuilder()

    if (includeHeaders) {
        for ((k, v) in level.getHeaders())
            builder.append("@").append(k).append(" ").append(v).append(lineSeparator)

        builder.append("---").append(lineSeparator)
    }

    if (includeData) {
        val blockMap = mutableMapOf<Block, String>()
        val blocks = level.blocks.sorted()

        for (block in blocks)
            if (blockMap.containsKey(block.block))
                blockMap[block.block] = blockMap[block.block] + "*" + block.coordinate.toString()
        else
            blockMap[block.block] = block.coordinate.toString()

        for ((k, v) in blockMap)
            builder.append(k).append(": ").append(v).append(lineSeparator)
    }

    builder.append("end")
    return builder.toString()
}