/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import kotlin.test.assertEquals

class TestMultiTypeEntitySelectorTransformation {

    @Test
    fun testTransform() {

        val lines = mutableListOf(
            "execute as @e[scores={n=8},type={zombie,creeper,spider,skeleton}] run function " +
                    "\$:do/a/thing @e[type={item,player}] word"
        )

        assertEquals(-1, MultiTypeEntitySelectorTransformation.transform(lines, 0, ""))
        assertEquals(
            mutableListOf(
                "execute as @e[scores={n=8},type=zombie] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=creeper] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=spider] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=skeleton] run function \$:do/a/thing @e[type={item,player}] word",
            ),
            lines
        )

        assertEquals(-1, MultiTypeEntitySelectorTransformation.transform(lines, 2, ""))
        assertEquals(
            mutableListOf(
                "execute as @e[scores={n=8},type=zombie] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=creeper] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=spider] run function \$:do/a/thing @e[type=item] word",
                "execute as @e[scores={n=8},type=spider] run function \$:do/a/thing @e[type=player] word",
                "execute as @e[scores={n=8},type=skeleton] run function \$:do/a/thing @e[type={item,player}] word",
            ),
            lines
        )

        // no change
        assertEquals(0, MultiTypeEntitySelectorTransformation.transform(lines, 2, ""))
        assertEquals(
            mutableListOf(
                "execute as @e[scores={n=8},type=zombie] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=creeper] run function \$:do/a/thing @e[type={item,player}] word",
                "execute as @e[scores={n=8},type=spider] run function \$:do/a/thing @e[type=item] word",
                "execute as @e[scores={n=8},type=spider] run function \$:do/a/thing @e[type=player] word",
                "execute as @e[scores={n=8},type=skeleton] run function \$:do/a/thing @e[type={item,player}] word",
            ),
            lines
        )
    }
}
