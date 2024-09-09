package dev.m13d.somenet.app

import kotlinx.coroutines.Dispatchers

class TestDispatchers: CoroutineDispatchers {

    override val background = Dispatchers.Unconfined
}
