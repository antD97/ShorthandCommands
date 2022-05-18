/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

/**
 * Transformation that replaces all occurrences of "__:" with the current namespace followed by colon
 * and all other occurrences of "__" with the current namespace followed by an underscore.
 */
object NamespacePrefixTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int {
        lines[i] = lines[i].replace("__:", "$namespace:")
        lines[i] = lines[i].replace("__", "${namespace}_")
        return 0
    }
}
