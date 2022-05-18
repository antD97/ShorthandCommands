package shorthandcommands.transformations

/**
 * Lines that end with #<num>x have that line repeated <num> times.
 * ^\s*#!(\d+)x\s
 */
object RepeatLineTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {

        val (matchString, n) = "^\\s*#!(\\d+)x\\s".toRegex().find(lines[i])?.groupValues ?: return 0

        val cleanedLine = lines[i].trimStart().removePrefix(matchString.trim()).trimStart()
        lines.removeAt(i)

        for (i in 1..n.toInt()) lines.add(i, cleanedLine)

        return 0
    }
}
