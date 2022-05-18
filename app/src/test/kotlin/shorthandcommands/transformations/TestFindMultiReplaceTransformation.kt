/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestFindMultiReplaceTransformation {

    @Test
    fun testTransform() {

        var lines = mutableListOf(
            "#!find=zombie",
            "#!replace=zombie|skeleton|creeper",
            "execute as @e[type=zombie] run say hi i'm a zombie"
        )

        assertEquals(-1, FindMultiReplaceTransformation.transform(lines, 0, ""))
        assertEquals(
            mutableListOf(
                "execute as @e[type=zombie] run say hi i'm a zombie",
                "execute as @e[type=skeleton] run say hi i'm a skeleton",
                "execute as @e[type=creeper] run say hi i'm a creeper"
            ),
            lines
        )

        // error checking
        lines = mutableListOf("#!find=zombie", "#!replace=zombie|skeleton|creeper")
        assertNull(FindMultiReplaceTransformation.transform(lines, 0, ""))

        lines = mutableListOf("#!find=zombie")
        assertNull(FindMultiReplaceTransformation.transform(lines, 0, ""))

        lines = mutableListOf(
            "#!find=zombie",
            "#!bad=zombie|skeleton|creeper",
            "execute as @e[type=zombie] run say hi i'm a zombie"
        )

        assertNull(FindMultiReplaceTransformation.transform(lines, 0, ""))
    }
}
