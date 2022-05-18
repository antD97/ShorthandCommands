/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import org.junit.Test
import shorthandcommands.Transformer
import kotlin.test.assertEquals

class TestFunctionDefinitionTransformation {

    @Test
    fun testTransform() {

        val lines = mutableListOf(
            "function namespace:some/function/name {   ",
            "    statement one",
            "    statement two",
            "    statement three",
            "    word word function namespace2:nested/function {   ",
            "        statement four",
            "        statement five",
            "        statement six",
            "        namespace3:create/only/function {   ",
            "            statement seven",
            "            statement eight",
            "            statement nine",
            "        }",
            "  #!  }",
            "}",
            "after the function body"
        )

        // nested create function only
        assertEquals(-1, FunctionDefinitionTransformation.transform(lines, 8, "namespace"))
        assertEquals(
            mutableListOf(
                "function namespace:some/function/name {   ",
                "    statement one",
                "    statement two",
                "    statement three",
                "    word word function namespace2:nested/function {   ",
                "        statement four",
                "        statement five",
                "        statement six",
                "  #!  }",
                "}",
                "after the function body"
            ),
            lines
        )

        var job = Transformer.createFunctionJobs.last()
        assertEquals("namespace3", job.namespace)
        assertEquals("create/only/function", job.functionPath)
        assertEquals("statement seven\nstatement eight\nstatement nine\n", job.file.readText())

        // create and run nested function with preceding words
        assertEquals(0, FunctionDefinitionTransformation.transform(lines, 4, "namespace"))
        assertEquals(
            mutableListOf(
                "function namespace:some/function/name {   ",
                "    statement one",
                "    statement two",
                "    statement three",
                "    word word function namespace2:nested/function",
                "}",
                "after the function body"
            ),
            lines
        )

        job = Transformer.createFunctionJobs.last()
        assertEquals("namespace2", job.namespace)
        assertEquals("nested/function", job.functionPath)
        assertEquals("statement four\nstatement five\nstatement six\n", job.file.readText())

        // create and run function with no preceding words
        assertEquals(0, FunctionDefinitionTransformation.transform(lines, 0, "namespace"))
        assertEquals(
            mutableListOf(
                "function namespace:some/function/name",
                "after the function body"
            ),
            lines
        )

        job = Transformer.createFunctionJobs.last()
        assertEquals("namespace", job.namespace)
        assertEquals("some/function/name", job.functionPath)
        assertEquals(
            "statement one\n" +
                    "statement two\n" +
                    "statement three\n" +
                    "word word function namespace2:nested/function\n",
            job.file.readText()
        )
    }
}
