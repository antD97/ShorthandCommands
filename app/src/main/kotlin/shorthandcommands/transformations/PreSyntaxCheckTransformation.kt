/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import shorthandcommands.printWarning

/**
 * Checks lines for syntax errors and print messages before any transformations are made. No changes
 * to [lines] are made.
 */
object PreSyntaxCheckTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {
        val line = lines[i]

        // ^\s*(#\s+!)
        "^\\s*(#\\s+!)".toRegex().find(line)?.groupValues?.get(1)?.let {
            printRecommendation(it, "#!", i)
        }

        if (line.contains("___")) {
            printRecommendation("___", "__", i)
        }

        return 0
    }

    internal fun printRecommendation(found: String, recommendation: String, i: Int) {
        printWarning("Found \"$found\" on line ${i + 1}. Did you mean \"$recommendation\"?")
    }
}
