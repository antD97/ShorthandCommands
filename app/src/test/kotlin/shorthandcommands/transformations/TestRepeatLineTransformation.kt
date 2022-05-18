/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import kotlin.test.assertEquals

class TestRepeatLineTransformation {

    @Test
    fun testTransform() {

        var lines = mutableListOf(
            "abc",
            "   Repeat this 5 times#5x",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        for (i in 1..5) assertEquals("   Repeat this 5 times", lines[i])
        assertEquals("123", lines[6])

        lines = mutableListOf(
            "abc",
            "   Nothing should happen to this line#1x",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        assertEquals("   Nothing should happen to this line", lines[1])
        assertEquals("123", lines[2])

        lines = mutableListOf(
            "abc",
            "   Remove this line#0x",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        assertEquals("123", lines[1])
    }
}
