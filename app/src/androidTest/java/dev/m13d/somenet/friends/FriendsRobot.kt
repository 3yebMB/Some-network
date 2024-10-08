package dev.m13d.somenet.friends

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.timeline.launchTimelineFor
import dev.m13d.somenet.utils.ComposeTestRule

fun launchFriends(
    rule: ComposeTestRule,
    block: FriendsRobot.() -> Unit,
): FriendsRobot {
    launchTimelineFor("email@mail.com", "Pas$123.", rule) {}
    return FriendsRobot(rule).apply {
        tapOnFriends()
        block()
    }
}

class FriendsRobot(
    private val rule: ComposeTestRule,
) {
    fun tapOnFriends() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onNodeWithText(friends)
            .performClick()
    }

    fun tapOnFollowFor(friend: Friend) {
        val followFriend = rule.activity.getString(R.string.followFriend, friend.user.id)
        rule.onNodeWithContentDescription(followFriend)
            .performClick()
    }

    fun tapOnUnfollowFor(friend: Friend) {
        val unfollowFriend = rule.activity.getString(R.string.unfollowFriend, friend.user.id)
        rule.onNodeWithContentDescription(unfollowFriend)
            .performClick()
    }

    infix fun verify(
        block: FriendsVerification.() -> Unit,
    ): FriendsVerification {
        return FriendsVerification(rule).apply(block)
    }
}

class FriendsVerification(
    private val rule: ComposeTestRule,
) {

    fun emptyFriendsMessageIsDisplayed() {
        val emptyFriendsMessage = rule.activity.getString(R.string.emptyFriendsMessage)
        rule.onNodeWithText(emptyFriendsMessage)
            .assertIsDisplayed()
    }

    fun friendsAreDisplayed(vararg friends: Friend) {
        friends.forEach { friend ->
            rule.onNodeWithText(friend.user.id)
                .assertIsDisplayed()
        }
    }

    fun friendInformationIsDisplayedFor(friend: Friend) {
        val follow = rule.activity.getString(R.string.follow)

        rule.onNodeWithText(friend.user.id)
            .assertIsDisplayed()
        rule.onNodeWithText(friend.user.about)
            .assertIsDisplayed()
        rule.onNodeWithText(follow)
            .assertIsDisplayed()
    }

    fun loadingIndicatorIsShown() {
        val loading = rule.activity.getString(R.string.loading)
        rule.onNodeWithContentDescription(loading)
            .assertIsDisplayed()
    }

    fun backendErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.fetchingFriendsError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun followingIsAddedFor(friend: Friend) {
        val unfollow = rule.activity.getString(R.string.unfollow)
        val unfollowFriend = rule.activity.getString(R.string.unfollowFriend, friend.user.id)
        rule.onNode(hasText(unfollow).and(hasContentDescription(unfollowFriend)))
            .assertIsDisplayed()
    }

    fun followingIsRemovedFor(friend: Friend) {
        val follow = rule.activity.getString(R.string.follow)
        val followFriend = rule.activity.getString(R.string.followFriend, friend.user.id)
        rule.onNode(hasText(follow).and(hasContentDescription(followFriend)))
            .assertIsDisplayed()
    }

    fun loadingIndicatorIsShownWhileTogglingFriendshipFor(friend: Friend) {
        val updatingFriendship = rule.activity.getString(R.string.updatingFriendship, friend.user.id)
        rule.onNodeWithContentDescription(updatingFriendship)
            .assertIsDisplayed()
    }

    fun backendErrorFollowingFriendIsDisplayed() {
        val errorFollowingFriend = rule.activity.getString(R.string.errorFollowingFriend)
        rule.onNodeWithText(errorFollowingFriend)
            .assertIsDisplayed()
    }
}
