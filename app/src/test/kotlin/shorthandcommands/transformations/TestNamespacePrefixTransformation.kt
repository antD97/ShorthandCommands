/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestNamespacePrefixTransformation {

    @Test
    fun testTransform() {

        var lines = mutableListOf("Any text $:function_name")
        assertEquals(0, NamespacePrefixTransformation.transform(lines, 0))
        assertEquals(mutableListOf("Any text null:function_name"), lines)

        lines = mutableListOf("Any text \$variable_name")
        assertEquals(0, NamespacePrefixTransformation.transform(lines, 0))
        assertEquals(mutableListOf("Any text null_variable_name"), lines)
    }
}
