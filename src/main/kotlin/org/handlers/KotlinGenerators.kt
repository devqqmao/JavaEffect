package org.handlers.handlers

public fun kotlinSequenceGenerator(n: Int): Sequence<Int> = sequence {
    for (i in 0 until n) {
        yield(i)
    }
}