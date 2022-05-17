/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

/**
 * When a line ends with a '\', the text on the following line has leading whitespace trimmed and
 * replaces the '\'.
 */
object LineBreakTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int {

        if (lines[i].endsWith('\\')) {

            lines[i] = lines[i].removeSuffix("\\")

            if (i + 1 <= lines.lastIndex) {
                lines[i] = "${lines[i]}${lines[i + 1].trimStart()}"
                lines.removeAt(i + 1)
            }

            // tell the transform loop to execute on this line again in case the line join added another
            // '\' to the end
            return -1
        }

        return 0
    }
}