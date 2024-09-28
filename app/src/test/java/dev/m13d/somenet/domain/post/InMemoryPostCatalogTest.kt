package dev.m13d.somenet.domain.post

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InMemoryPostCatalogTest {

    private val tomId = "tomId"
    private val annaPost = Post("annaPostId", "annaId", "Post by Anna", 1L)
    private val tomPost = Post("tomPostId", tomId, "Post by Tom", 2L)
    private val lucyPost = Post("lucyPostId", "lucyId", "Post by Lucy", 3L)

    @Test
    fun postFound() = runBlocking {
        val postCatalog = postCatalogWith(annaPost, tomPost, lucyPost)

        val result = postCatalog.postsFor(listOf(tomId))

        assertEquals(listOf(tomPost), result)
    }

    @Test
    fun postNotFound() = runBlocking {
        val postCatalog = postCatalogWith(annaPost, lucyPost)

        val result = postCatalog.postsFor(listOf(tomId))

        assertEquals(emptyList(), result)
    }

    @Test
    fun addNewPost() = runBlocking {
        val postCatalog = postCatalogWith(annaPost, lucyPost)
        val newAddedPost = postCatalog.addPost(tomId, "Tom's new post")

        val result = postCatalog.postsFor(listOf(tomId))

        assertEquals(listOf(newAddedPost), result)
    }

    private fun postCatalogWith(
        vararg availablePosts: Post
    ): PostsCatalog {
        return InMemoryPostsCatalog(
            availablePosts = availablePosts.toMutableList()
        )
    }
}
