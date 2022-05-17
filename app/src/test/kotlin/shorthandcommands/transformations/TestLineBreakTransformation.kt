/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestLineBreakTransformation {

    @Test
    fun testTransform() {

        val lines = mutableListOf(
            "Any text can go here\\",
            "    , and here's some more text. \\",
            "    Another line!"
        )
        assertEquals(-1, LineBreakTransformation.transform(lines, 0, ""))
        assertEquals(
            mutableListOf(
                "Any text can go here, and here's some more text. \\",
                "    Another line!"
            ),
            lines
        )

        assertEquals(-1, LineBreakTransformation.transform(lines, 0, ""))
        assertEquals(
            mutableListOf("Any text can go here, and here's some more text. Another line!"),
            lines
        )

        assertEquals(0, LineBreakTransformation.transform(lines, 0, ""))
        assertEquals(
            mutableListOf("Any text can go here, and here's some more text. Another line!"),
            lines
        )
    }
}
