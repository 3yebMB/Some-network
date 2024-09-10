package dev.m13d.somenet.domain.post

class InMemoryPostsCatalog {
    fun postsFor(userIds: List<String>): List<Post> {
        val availablePosts = listOf(
            Post("post2", "lucyId", "post 2", 2L),
            Post("post1", "lucyId", "post 1", 1L),
            Post("postId", "timId", "post text", 1L),
            Post("post4", "sarahId", "post 4", 4L),
            Post("post3", "sarahId", "post 3", 3L),
        )
        return availablePosts.filter { userIds.contains(it.userId) }
    }
}
