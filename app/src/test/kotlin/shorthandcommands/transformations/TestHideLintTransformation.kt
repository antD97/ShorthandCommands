/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import kotlin.test.assertEquals

class TestHideLintTransformation {

    @Test
    fun testTransform() {

        val lines = mutableListOf(
            "     #!   Line 1",
            "    # Don't touch this line!",
            "#!Line 2",
            "Don't touch this line either!",
        )

        assertEquals(0, HideLintTransformation.transform(lines, 0, ""))
        assertEquals("Line 1", lines[0])

        assertEquals(0, HideLintTransformation.transform(lines, 1, ""))
        assertEquals("    # Don't touch this line!", lines[1])

        assertEquals(0, HideLintTransformation.transform(lines, 2, ""))
        assertEquals("Line 2", lines[2])

        assertEquals(0, HideLintTransformation.transform(lines, 3, ""))
        assertEquals("Don't touch this line either!", lines[3])
    }
}
