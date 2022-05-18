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
            " #!11x  Repeat this 11 times   ",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        for (i in 1..11) assertEquals("Repeat this 11 times   ", lines[i])
        assertEquals("123", lines[12])

        lines = mutableListOf(
            "abc",
            "   #!1x Nothing should happen to this line",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        assertEquals("Nothing should happen to this line", lines[1])
        assertEquals("123", lines[2])

        lines = mutableListOf(
            "abc",
            "   #!0x Remove this line",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals("abc", lines[0])
        assertEquals("123", lines[1])

        lines = mutableListOf(
            "abc",
            "   #!0xA Space is required",
            "123",
        )

        assertEquals(0, RepeatLineTransformation.transform(lines, 1, ""))
        assertEquals( "   #!0xA Space is required", lines[1])
    }
}
