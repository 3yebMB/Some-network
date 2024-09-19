package dev.m13d.somenet.infrastructure

import dev.m13d.somenet.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.regex.Pattern
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class UUIDGeneratorTest {

    private companion object {
        private const val UUID_REGEX = """[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"""
    }

    @Test
    fun generateCorrectUUID() {
        val uuid = UUIDGenerator().next()
        val pattern = Pattern.compile(UUID_REGEX)
        assertTrue(pattern.matcher(uuid).matches())
    }
}
