/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import shorthandcommands.printError

/**
 * Pattern regex: `^\s*#!find=(.*)$`
 * Replace regex: `^\s*#!replace=(.*)$`
 */
object FindMultiReplaceTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {

        val (patternCommand, pattern) = "^\\s*#!find=(.*)\$".toRegex()
            .find(lines[i])?.groupValues
            ?.mapIndexed { index, s -> if (index == 0) s.trimStart() else s }
            ?: return 0

        if (i + 2 > lines.lastIndex) {
            printError("There needs to be at least two lines following \"$patternCommand\".")
            return null
        }

        val (_, replaceValues) = "^\\s*#!replace=(.*)\$".toRegex()
            .find(lines[i + 1])?.groupValues
            ?.mapIndexed { _i, s -> if (_i == 0) s.trimStart() else s }
            ?: run {
                printError("Could not parse \"${lines[i + 1]}\".\n")
                return null
            }

        // remove pattern & replace lines
        lines.removeAt(i)
        lines.removeAt(i)
        // remove repeated line
        val originalLine = lines.removeAt(i)

        // add repeated lines
        for (replaceValue in replaceValues.split("|").reversed()) {
            lines.add(i, originalLine.replace(pattern, replaceValue))
        }

        return -1
    }
}
