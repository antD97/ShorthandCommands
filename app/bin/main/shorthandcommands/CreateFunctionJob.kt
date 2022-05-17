/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands

import java.io.File

data class CreateFunctionJob(
    /** File containing the function's commands. */
    val file: File,
    /** The name for the function. */
    val namespace: String,
    val functionPath: String
)
