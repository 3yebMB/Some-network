package dev.m13d.somenet.app

import kotlinx.coroutines.Dispatchers

class DefaultDispatchers : CoroutineDispatchers {

    override val background = Dispatchers.IO
}
