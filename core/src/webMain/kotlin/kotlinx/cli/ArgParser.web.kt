/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026-10-24
 */

package kotlinx.cli

internal actual fun exitProcess(status: Int): Nothing {
    error("Not implemented for Web targets")
}