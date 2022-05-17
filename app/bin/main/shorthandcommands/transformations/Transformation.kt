/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

interface Transformation {
    /**
     * Reads line [i] in [lines] and tries to apply this transformation.
     * @return the number of lines to skip or null if there was an issue parsing the text
     */
    fun transform(lines: MutableList<String>, i: Int, namespace: String): Int?
}
