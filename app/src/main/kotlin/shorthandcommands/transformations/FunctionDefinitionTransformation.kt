/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import shorthandcommands.CreateFunctionJob
import shorthandcommands.Transformer
import java.io.File
import java.io.FileWriter

/**
 * Transformation that takes function bodies and creates new .mcfunction files for them.
 * - (?:^|^.*\s*)function (\S+):(\S+) \{\s*$ function that's run immediately
 * - ^\s*(\S+):(\S+) \{\s*$ function that is created, but not run immediately
 */
object FunctionDefinitionTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {

        val runImmediatelyGroups = "(?:^|^.*\\s*)function (\\S+):(\\S+) \\{\\s*\$".toRegex().find(lines[i])?.groupValues
        val createOnlyGroups = "^\\s*(\\S+):(\\S+) \\{\\s*\$".toRegex().find(lines[i])?.groupValues

        val (_, namespace, functionPath) = when {
            runImmediatelyGroups != null -> {
                // remove trailing " {"
                lines[i] = lines[i].trimEnd().removeSuffix(" {")
                runImmediatelyGroups
            }
            createOnlyGroups != null -> createOnlyGroups
            else -> return 0 // regex doesn't match
        }

        // find closing }
        var depth = 0
        var found = false
        var j = i + 1

        while (!found && j <= lines.lastIndex) {
            for (c in lines[j]) {
                when {
                    c == '{' -> depth++
                    c == '}' && depth > 0 -> depth--
                    c == '}' && depth == 0 -> {
                        found = true
                        break
                    }
                }
            }

            if (!found) j++
        }

        // no matching }
        if (!found) {
            println(
                "Could not find the matching bracket for the function body \"$functionPath\".\n" +
                        "Exiting..."
            )
            return null
        }

        // matching } on its own line
        if (lines[j].trim() != "}") {
            println(
                "Closing bracket for \"$functionPath\" needs to be on its own line.\n" +
                        "Exiting..."
            )
            return null
        }

        // remove closing bracket line
        lines.removeAt(j)
        j--

        // save lines i+1->j as a CreateFunctionJob
        val tempFile = File.createTempFile(functionPath, ".mcfunction")
        FileWriter(tempFile).use {
            for (k in (i + 1)..j) {
                it.write("${lines[k].trimStart()}\n")
            }
        }

        Transformer.createFunctionJobs.add(CreateFunctionJob(tempFile, namespace, functionPath))

        // remove lines i+1->j
        for (k in j downTo (i + 1)) lines.removeAt(k)

        return when {
            runImmediatelyGroups != null -> 0
            createOnlyGroups != null -> {
                // remove the line completely
                lines.removeAt(i)
                -1
            }
            else -> throw java.lang.IllegalStateException()
        }
    }
}
