package dev.m13d.somenet.infrastructure

class ControllableClock(
    private val timestamp: Long,
) {
    fun now(): Long {
        return timestamp
    }
}
