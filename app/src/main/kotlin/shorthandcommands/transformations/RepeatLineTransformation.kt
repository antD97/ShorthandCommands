package shorthandcommands.transformations

/**
 * Lines that follow the format `#!<n>x` will be removed and repeat the following line `<n>` times.
 * - Regex: `^\s*#!(\d+)x(?=\s*$)`
 */
object RepeatLineTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {

        val (fullCommand, n) = "^\\s*#!(\\d+)x(?=\\s*\$)".toRegex()
            .find(lines[i])?.groupValues
            ?.mapIndexed { _i, s -> if (_i == 0) s.trimStart() else s }
            ?: return 0

        if (i == lines.lastIndex) {
            println("No line following \"$fullCommand\" to repeat." +
                    "\nExiting...")
            return null
        }

        // remove repeat command
        lines.removeAt(i)

        // add repeated lines
        val lineToRepeat = lines[i]
        for (i in 1 until n.toInt()) lines.add(i, lineToRepeat)

        return -1
    }
}
