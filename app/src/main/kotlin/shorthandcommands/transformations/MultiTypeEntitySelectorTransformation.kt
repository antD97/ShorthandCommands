/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

/**
 * @e\[[^\]]*type=\{([^\}]*)\}\]
 */
object MultiTypeEntitySelectorTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int {

        val (_, types) = "@e\\[[^\\]]*type=\\{([^\\}]*)\\}\\]".toRegex()
            .find(lines[i])?.groupValues ?: return 0

        val originalLine = lines.removeAt(i)

        for (type in types.split(",").reversed()) {
            lines.add(i, originalLine.replaceFirst("{$types}", type))
        }

        return -1
    }
}
