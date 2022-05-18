/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestRepeatLineTransformation {

    @Test
    fun testTransform() {

        var lines = mutableListOf(
            "abc",
            "  #!11x  ",
            "   Repeat this 11 times   ",
            "123",
        )

        assertEquals(-1, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        for (i in 1..11) assertEquals("   Repeat this 11 times   ", lines[i])
        assertEquals("123", lines[12])

        // error checking
        assertNull(RepeatLineTransformation.transform(mutableListOf("abc", "  #!11x  "), 1, ""))
        
        lines = mutableListOf("abc", "b#!11x", "123")
        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals(mutableListOf("abc", "b#!11x", "123"), lines)
    }
}
