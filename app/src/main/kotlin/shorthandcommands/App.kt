/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands

import java.io.File
import java.io.IOException
import java.io.StringReader
import java.util.*
import kotlin.system.exitProcess

fun main() {

    // load from properties file
    val properties = Properties()

    val propertiesFile = File("shorthand.conf")
    if (!propertiesFile.isFile) {
        printError("Could not locate configuration file `${propertiesFile.absolutePath}`.")
        exitProcess(-1)
    }

    // replace \ with \\ before loading the configuration file
    propertiesFile.readText()
        .replace("\\", "\\\\")
        .let { StringReader(it).use { sr -> properties.load(sr) } }

    val projDir = File(properties.getProperty("project") ?: badConfig("project"))

    // project directory check
    if (!projDir.isDirectory) {
        printError("Project directory `${projDir.path}` from `shorthand.conf` is not a directory.")
        exitProcess(-1)
    }

    val saveDir = File(properties.getProperty("save") ?: badConfig("save"))

    // save directory check
    if (!saveDir.isDirectory) {
        printError("Save directory `${saveDir.path}` from `shorthand.conf` is not a directory.")
        exitProcess(-1)
    }

    val transformedProjDir = saveDir.resolve(projDir.name)

    // transformed project directory check
    if (transformedProjDir.isFile) {
        printError("The save location for the converted project `${transformedProjDir.path}` is " +
                "a file and will not be overwritten. Please move or delete this file and run the " +
                "tool again.")
        exitProcess(-1)
    }

    // transformed project directory = save directory check
    if (projDir.canonicalFile == transformedProjDir.canonicalFile) {
        printError("The save location for the converted project is the same as the project " +
                "directory `${projDir.path}`. Please change the save location in " +
                "`shorthand.conf` file and run the tool again.")
        exitProcess(-1)
    }

    // data directory & `pack.mcmeta` check
    val projDataDir = projDir.listFiles().find { it.name == "data" && it.isDirectory }
    val projPackFile = projDir.listFiles().find { it.name == "pack.mcmeta" && it.isFile }
    if (projDataDir == null || projPackFile == null) {
        printError("The project directory `${projDir.path}` needs to contain a data directory " +
                "and `pack.mcmeta` file.")
        exitProcess(-1)
    }

    var headerFile: File? = File("header.txt")

    // header file check
    if (!headerFile!!.isFile) {
        printWarning("Could not locate a `header.txt` file. No header will be added for newly " +
                "created .mcfunction files.")
        headerFile = null
    }

    // delete old transformed project directory
    if (transformedProjDir.exists()) {

        val transformedProjPackFile = transformedProjDir.listFiles()
            .find { it.name == "pack.mcmeta" && it.isFile }

        // directory to overwrite is not a datapack
        if (transformedProjDir.listFiles().none { it.name == "data" && it.isDirectory }
            || transformedProjPackFile == null) {
            printError("The save location for the converted project `${transformedProjDir.path}` " +
                    "is not a datapack and will not be overwritten. Please move or delete this " +
                    "directory and run the tool again.")
            exitProcess(-1)
        }

        // directory to overwrite does not use the same `pack.mcmeta` file
        else if (projPackFile.readText() != transformedProjPackFile.readText()) {
            printError("The save location for the converted project `${transformedProjDir.path}` " +
                    "has a different `pack.mcmeta` file than the source project " +
                    "`${projDir.path}` and will not be overwritten. Please move or delete this " +
                    "project directory and run the tool again.")
            exitProcess(-1)
        }

        // directory to overwrite is the same datapack and is safe to overwrite
        else {
            print("Okay to delete the previously converted project " +
                    "`${transformedProjDir.path}`? (y/n): ")
            if (readLine()!!.lowercase() in listOf("y", "yes")) {

                println("Deleting `$transformedProjDir`...")
                if (!transformedProjDir.deleteRecursively()) {
                    printError("Failed to delete `${transformedProjDir.path}`")
                    exitProcess(-1)
                }
            } else {
                println("Exiting...")
                exitProcess(-1)
            }
        }
    }

    // find each functions directory
    val projFunctionsDirs = projDataDir.listFiles()
        .filter { it.name != "minecraft" && it.isDirectory }
        .filter {
            val containsFunctionsDir = it.listFiles().any {
                it.name == "functions" && it.isDirectory
            }
            if (!containsFunctionsDir) printWarning("Namespace directory `${it.path}` does not " +
                    "contain a `function` directory. Skipping...")
            containsFunctionsDir
        }
        .map { it.resolve("functions") }

    // copy the source project directory
    println("Copying source project...")
    projDir.copyDir(transformedProjDir, projFunctionsDirs)

    // transform function directories
    for (functionDir in projFunctionsDirs) {
        Transformer.transformFunctionDir(
            functionDir,
            transformedProjDir.resolve(functionDir.relativeTo(projDir)),
            headerFile
        )
    }

    println("Done.")
}

fun badConfig(property: String): String {
    printError("\"$property\" missing from `shorthand.conf`.")
    exitProcess(-1)
}

/** Recursively copies [this] file/directory to [targetLoc] while skipping [skipFiles]. */
fun File.copyDir(targetLoc: File, skipFiles: List<File>) {

    // skip
    if (this in skipFiles) return

    // copy directory
    if (isDirectory) {
        try {
            if (!targetLoc.mkdir()) {
                printError("Failed to create directory: `${targetLoc.path}`")
                exitProcess(-1)
            }
        } catch (e: IOException) {
            printError("Failed to create directory: `${targetLoc.path}`")
            exitProcess(-1)
        }

        this.listFiles().forEach { it.copyDir(targetLoc.resolve(it.name), skipFiles) }
    }

    // copy file
    else {
        try {
            this.copyTo(targetLoc)
        } catch (e: IOException) {
            printError("Failed to create file: `${targetLoc.name}`")
            exitProcess(-1)
        }
    }
}
