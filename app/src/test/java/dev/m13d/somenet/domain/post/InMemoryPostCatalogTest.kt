package dev.m13d.somenet.domain.post

class InMemoryPostCatalogTest : PostCatalogContract() {

    override fun postCatalogWith(
        vararg availablePosts: Post,
    ): PostsCatalog {
        return InMemoryPostsCatalog(
            availablePosts = availablePosts.toMutableList()
        )
    }
}
