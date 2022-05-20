/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands.transformations

import shorthandcommands.Transformer
import shorthandcommands.printError
import java.io.File
import java.io.FileWriter

/**
 * Transformation that takes function bodies and creates new .mcfunction files for them.
 * - Define & execute regex: `(?<=^|\s)function (\S+):(\S+)(?=\s|$)`
 * - Define only regex: `^\s*(\S+):(\S+)(?=\s|$)` function that is created, but not run immediately
 */
internal object FunctionDefinitionTransformation : Transformation {

    override fun transform(lines: MutableList<String>, i: Int, namespace: String): Int? {

        if (lines[i].trim() != "{") return 0

        if (i == 0) {
            printError("No line preceding { to create a function definition.")
            return null
        }

        val (fullDefinition, functionNamespace,functionPath) =
            "(?<=^|\\s)function (\\S+):(\\S+)(?=\\s|\$)".toRegex()
                .find(lines[i - 1])?.groupValues
                ?: "^\\s*(\\S+):(\\S+)(?=\\s|\$)".toRegex()
                    .find(lines[i - 1])?.groupValues
                    ?.mapIndexed { _i, s -> if (_i == 0) s.trimStart() else s }
                ?: run {
                    printError("Could not parse function definition from \"${lines[i - 1]}\".")
                    return null
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
            printError("Could not find the matching bracket for the function body \"$functionPath\".")
            return null
        }

        // check matching } is on its own line
        if (lines[j].trim() != "}") {
            printError("Closing bracket for \"$functionPath\" needs to be on its own line.")
            return null
        }

        // remove brackets
        lines.removeAt(j)
        lines.removeAt(i)
        j -= 2

        // save lines i->j as a CreateFunctionJob
        val tempFile = File.createTempFile(functionPath, ".mcfunction")
        FileWriter(tempFile).use {
            for (k in i..j) it.write("${lines[k].trimStart()}\n")
        }

        Transformer.createFunctionJobs.add(
            Transformer.CreateFunctionJob(tempFile, functionNamespace, functionPath)
        )

        // remove lines i->j
        for (k in j downTo i) lines.removeAt(k)

        return if (fullDefinition.startsWith("function ")) -1
        else {
            // remove definition line
            lines.removeAt(i - 1)
            -2
        }
    }
}
