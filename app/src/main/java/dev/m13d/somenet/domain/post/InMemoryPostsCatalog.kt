package dev.m13d.somenet.domain.post

class InMemoryPostsCatalog(
    private val availablePosts: List<Post> = emptyList(),
) : PostsCatalog {

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        return availablePosts.filter { userIds.contains(it.userId) }
    }
}
