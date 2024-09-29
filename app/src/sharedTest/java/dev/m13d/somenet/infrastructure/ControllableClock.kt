package dev.m13d.somenet.infrastructure

class ControllableClock(
    private val timestamp: Long,
) : Clock {

    override fun now(): Long {
        return timestamp
    }
}
