/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands

import shorthandcommands.transformations.FunctionDefinitionTransformation
import shorthandcommands.transformations.LineBreakTransformation
import shorthandcommands.transformations.NamespacePrefixTransformation
import shorthandcommands.transformations.ScoreboardExpressionTransformation
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.system.exitProcess

object Transformer {

    /** Transformations to apply when calling [applyTransformations]. */
    private val transformations = listOf(
        LineBreakTransformation, // must be applied first
        FunctionDefinitionTransformation, // should go before NamespacePrefixTransformation
        NamespacePrefixTransformation,
        ScoreboardExpressionTransformation
    )

    /** List of additional .mcfunction files to create. */
    internal val createFunctionJobs = mutableListOf<CreateFunctionJob>()

    /** Completely transforms a datapack's function directory. */
    internal fun transformFunctionDir(functionDir: File, targetLoc: File, headerFile: File?) {

        // apply transformations to each file [functionDir]
        functionDir.recursivelyTransform(targetLoc, functionDir.parentFile.name)

        // create new function files
        val newDataDir = targetLoc.parentFile.parentFile

        while (createFunctionJobs.isNotEmpty()) {
            val job = createFunctionJobs.removeFirst()

            println("Creating new function \"${job.namespace}:${job.functionPath}\"...")

            val newFunctionFile = newDataDir
                .resolve(job.namespace)
                .resolve("functions/${job.functionPath}.mcfunction")

            // ensure the function file has a directory to go into
            if (!newFunctionFile.parentFile.exists()) {
                try {
                    newFunctionFile.parentFile.mkdirs()
                } catch (e: IOException) {
                    println("Failed to create directory: `${newFunctionFile.parentFile.path}`\n" +
                            "Exiting...")
                    exitProcess(-1)
                }
            }

            // file already exists
            if (newFunctionFile.exists()) {
                println("Cannot create new function file `${newFunctionFile.path}` because it" +
                        "already exists.\n" +
                        "Exiting...")
                exitProcess(-1)
            }

            // apply transformations to the new function file
            job.file.applyTransformations(newFunctionFile, job.namespace)

            // add the header file text to the new function file
            if (headerFile != null) {
                val tempFile = File.createTempFile("temp", null)
                FileWriter(tempFile).use {
                    it.write(headerFile.readText() + "\n")
                    it.write(newFunctionFile.readText())
                }

                try {
                    tempFile.copyTo(newFunctionFile, true)
                } catch (e: IOException) {
                    println("Failed to copy file to: `${tempFile.name}`\nExiting...")
                    exitProcess(-1)
                }
            }
        }
    }

    /**
     * Recursively run [applyTransformations] on all `mcfunction` files in [this] and saves them in
     * [targetLoc].
     */
    private fun File.recursivelyTransform(targetLoc: File, nameSpace: String) {

        // copy directory and transform containing files
        if (isDirectory) {

            // copy
            try {
                if (!targetLoc.mkdir()) {
                    println("Failed to create directory: `${targetLoc.path}`\nExiting...")
                }
            } catch (e: SecurityException) {
                println("Security violation while trying to create directory: " +
                        "`${targetLoc.path}`\n" +
                        "Exiting...")
                exitProcess(-1)
            }

            // transform files
            for (f in this.listFiles()) {
                f.recursivelyTransform(targetLoc.resolve(f.name), nameSpace)
            }
        }
        // transform .mcfunction files
        else if (this.name.endsWith(".mcfunction")) {
            this.applyTransformations(targetLoc, nameSpace)
        }
        // copy all other files
        else {
            try {
                this.copyTo(targetLoc)
            } catch (e: IOException) {
                println("Failed to create file: `${targetLoc.name}`\nExiting...")
                exitProcess(-1)
            }
        }
    }

    /**
     * Applies [transformations] to [this] and saves the result to [targetLoc].
     */
    private fun File.applyTransformations(targetLoc: File, nameSpace: String) {
        println("Transforming `${this.path}`...")

        // transform lines
        val lines = this.readLines().toMutableList()
        var i = 0
        while (i <= lines.lastIndex) {

            for (transformation in transformations) {
                i += transformation.transform(lines, i, nameSpace) ?: exitProcess(-1)
            }

            i++
        }

        // write lines
        FileWriter(targetLoc).use {
            for (line in lines) {
                it.write("$line\n")
            }
        }
    }
}
