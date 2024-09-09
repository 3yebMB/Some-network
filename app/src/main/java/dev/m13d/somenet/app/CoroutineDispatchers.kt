package dev.m13d.somenet.app

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    val background: CoroutineDispatcher
}
