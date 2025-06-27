package org.handlers.handlers

// Явно укажите public для классов и функций
public fun kotlinSequenceGenerator(n: Int): Sequence<Int> = sequence {
    for (i in 0 until n) {
        yield(i)
    }
}

public class KotlinBasicGenerator(public val n: Int) : Iterator<Int> {
    private var count = 0

    override fun hasNext(): Boolean = count < n

    override fun next(): Int = count++
}