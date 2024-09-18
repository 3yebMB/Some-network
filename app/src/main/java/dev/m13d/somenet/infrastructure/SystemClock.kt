package dev.m13d.somenet.infrastructure

class SystemClock : Clock {

    override fun now(): Long {
        return System.currentTimeMillis()
    }
}
