/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import shorthandcommands.component6

/**
 * scoreboard players <set,add,remove> <targets> <objective> <score>
 * #!sb <targets> <objective> < =, +=, -= > <score>
 * (?:\s|^)#!sb (\S+) (\S+) (=|\+=|-=) (-?\d+)(?:\s|$)
 *
 * scoreboard players operation <targets> <targetObjective> <operation> <source> <sourceObjective>
 * #!sb <targets> <targetObjective> <operation> <source> <sourceObjective>
 * (?:\s|^)#!sb (\S+) (\S+) (\+=|-=|\*=|\/=|%=|=|<|>|><) (\S+) (\S+)(?:\s|$)
 *
 * scoreboard players reset <targets> [<objective>]
 * #!sb reset <targets> [<objective>]
 * (?:\s|^)#!sb reset (\S+)( \S+)?(?:\s|$)
 *
 * scoreboard players enable <targets> <objective>
 * #!sb enable <targets> <objective>
 * (?:\s|^)#!sb enable (\S+) (\S+)(?:\s|$)
 */
object ScoreboardExpressionTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int {

        // operation with score
        val scoreOperationMatchGroups = "(?:\\s|^)#!sb (\\S+) (\\S+) (=|\\+=|-=) (-?\\d+)(?:\\s|\$)"
            .toRegex()
            .find(lines[i])
            ?.groupValues

        if (scoreOperationMatchGroups != null) {
            val targets = scoreOperationMatchGroups[1]
            val objective = scoreOperationMatchGroups[2]
            val operation = when (scoreOperationMatchGroups[3]) {
                "=" -> "set"
                "+=" -> "add"
                "-=" -> "remove"
                else -> throw IllegalStateException("Unexpected value " +
                        "\"${scoreOperationMatchGroups[3]}\" in regex group")
            }
            val score = scoreOperationMatchGroups[4]

            lines[i] = lines[i].replace(
                scoreOperationMatchGroups[0].trim(),
                "scoreboard players $operation $targets $objective $score"
            )

            return -1
        }

        // operation with players
        val playersOperationMatchGroups =
            "(?:\\s|^)#!sb (\\S+) (\\S+) (\\+=|-=|\\*=|\\/=|%=|=|<|>|><) (\\S+) (\\S+)(?:\\s|\$)"
                .toRegex()
                .find(lines[i])
                ?.groupValues

        if (playersOperationMatchGroups != null) {
            val (_, targets, targetObjective, operation, source, sourceObjective) =
                playersOperationMatchGroups

            lines[i] = lines[i].replace(
                playersOperationMatchGroups[0].trim(),
                "scoreboard players operation $targets $targetObjective $operation $source $sourceObjective"
            )

            return -1
        }

        // reset
        val resetMatchGroups = "(?:\\s|^)#!sb reset (\\S+)( \\S+)?(?:\\s|\$)".toRegex()
            .find(lines[i])
            ?.groupValues

        if (resetMatchGroups != null) {
            val targets = resetMatchGroups[1]
            val objective = resetMatchGroups[2].trimStart()

            lines[i] = lines[i].replace(
                resetMatchGroups[0].trim(),
                if (objective != "") "scoreboard players reset $targets $objective"
                else "scoreboard players reset $targets"
            )

            return -1
        }

        // enable
        val enableMatchGroups = "(?:\\s|^)#!sb enable (\\S+) (\\S+)(?:\\s|\$)".toRegex()
            .find(lines[i])
            ?.groupValues

        if (enableMatchGroups != null) {
            val targets = enableMatchGroups[1]
            val objective = enableMatchGroups[2]

            lines[i] = lines[i].replace(
                enableMatchGroups[0].trim(),
                "scoreboard players enable $targets $objective"
            )

            return -1
        }

        return 0
    }
}
