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

        var lines = mutableListOf("Any text __:function_name")
        assertEquals(0, NamespacePrefixTransformation.transform(lines, 0, "some_namespace"))
        assertEquals(mutableListOf("Any text some_namespace:function_name"), lines)

        lines = mutableListOf("Any text __variable_name")
        assertEquals(0, NamespacePrefixTransformation.transform(lines, 0, "another_namespace"))
        assertEquals(mutableListOf("Any text another_namespace_variable_name"), lines)
    }
}
