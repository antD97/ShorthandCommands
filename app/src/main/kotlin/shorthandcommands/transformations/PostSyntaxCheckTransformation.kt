/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import shorthandcommands.printError

/**
 * Checks lines for syntax errors and print messages after all transformations were made. No changes
 * to [lines] are made.
 */
object PostSyntaxCheckTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {
        val line = lines[i]

        if (line.trimStart().startsWith("#!")) {
            printError("Failed to parse \"$line\".")
            return null
        }

        return 0
    }
}
