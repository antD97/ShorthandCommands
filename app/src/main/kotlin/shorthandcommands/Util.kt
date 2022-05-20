/*
 * Copyright © 2022 antD97
 * Licensed under the MIT License https://antD.mit-license.org/
 */
package shorthandcommands

import kotlin.system.exitProcess

internal operator fun <E> List<E>.component6(): E = this[5]

internal fun printWarning(s: String) = println("[ WARNING ] $s")

internal fun printError(s: String) = println("[ ERROR ] $s\nExiting...")
