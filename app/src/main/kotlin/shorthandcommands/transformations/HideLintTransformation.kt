/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

/** Lines that start with "#! " have the "#! " removed along with any leading whitespace. */
object HideLintTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {

        if (lines[i].trimStart().startsWith("#! ")) {
            lines[i] = lines[i].trimStart().removePrefix("#! ").trimStart()
        }

        return 0
    }
}
