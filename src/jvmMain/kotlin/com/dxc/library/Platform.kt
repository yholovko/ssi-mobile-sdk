package com.dxc.library

actual class Platform actual constructor() {
    actual val platform: String
        get() = "JVM"
}