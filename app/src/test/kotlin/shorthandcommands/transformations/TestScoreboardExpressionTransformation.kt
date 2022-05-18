/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import kotlin.test.assertEquals

class TestScoreboardExpressionTransformation {

    @Test
    fun testTransform() {

        val lines = mutableListOf(
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective = 222",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective += -222",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective -= 222",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 += @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 -= @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 *= @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 /= @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 %= @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 = @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 < @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 > @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb @s[type=creeper,scores={objective=-111}] \$objective1 >< @s[type=zombie,scores={objective=-222}] \$objective2",
            "#!sb reset @s[type=creeper,scores={objective=-111}] \$objective",
            "#!sb reset @s[type=creeper,scores={objective=-111}]"
        )

        // operation with score
        assertEquals(-1, ScoreboardExpressionTransformation.transform(lines, 0, ""))
        assertEquals(
            "scoreboard players set @s[type=creeper,scores={objective=-111}] \$objective 222",
            lines[0]
        )

        assertEquals(-1, ScoreboardExpressionTransformation.transform(lines, 1, ""))
        assertEquals(
            "scoreboard players add @s[type=creeper,scores={objective=-111}] \$objective -222",
            lines[1]
        )

        assertEquals(-1, ScoreboardExpressionTransformation.transform(lines, 2, ""))
        assertEquals(
            "scoreboard players remove @s[type=creeper,scores={objective=-111}] \$objective 222",
            lines[2]
        )

        // operation with players
        val operations = listOf("+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><")
        for (i in 3..11) {
            assertEquals(-1, ScoreboardExpressionTransformation.transform(lines, i, ""))
            assertEquals(
                "scoreboard players operation @s[type=creeper,scores={objective=-111}] " +
                        "\$objective1 ${operations[i - 3]} " +
                        "@s[type=zombie,scores={objective=-222}] \$objective2",
                lines[i]
            )
        }

        // reset
        assertEquals(-1, ScoreboardExpressionTransformation.transform(lines, 12, ""))
        assertEquals(
            "scoreboard players reset @s[type=creeper,scores={objective=-111}] \$objective",
            lines[12]
        )

        assertEquals(-1, ScoreboardExpressionTransformation.transform(lines, 13, ""))
        assertEquals(
            "scoreboard players reset @s[type=creeper,scores={objective=-111}]",
            lines[13]
        )
    }
}
