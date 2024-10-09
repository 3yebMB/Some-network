package dev.m13d.somenet.friends

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.friends.states.FriendsScreenState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.ScreenTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun FriendsScreen(userId: String) {

    val friendsViewModel = koinViewModel<FriendsViewModel>()
    if (friendsViewModel.screenState.value == null) {
        friendsViewModel.loadFriends(userId)
    }
    val screenState = friendsViewModel.screenState.observeAsState().value ?: FriendsScreenState()

    FriendsScreenContent(
        screenState = screenState,
        onRefresh = { friendsViewModel.loadFriends(userId) },
        toggleFollowingFor = { friendsViewModel.toggleFollowing(userId, it) }
    )
}

@Composable
private fun FriendsScreenContent(
    modifier: Modifier = Modifier,
    screenState: FriendsScreenState,
    onRefresh: () -> Unit,
    toggleFollowingFor: (String) -> Unit,
) {
    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(titleResource = R.string.friends)
            Spacer(modifier = Modifier.height(16.dp))
            FriendsList(
                isRefreshing = screenState.isLoading,
                friends = screenState.friends,
                onRefresh = { onRefresh() },
                toggleFollowingFor = { toggleFollowingFor(it) }
            )
        }
        InfoMessage(stringResource = screenState.error)
    }
}

@Composable
private fun FriendsList(
    isRefreshing: Boolean,
    friends: List<Friend>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    toggleFollowingFor: (String) -> Unit,
) {
    val description = stringResource(R.string.loading)
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh() },
        modifier = modifier.semantics { contentDescription = description },
    ) {
        if (friends.isEmpty()) {
            Text(
                text = stringResource(R.string.emptyFriendsMessage),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(friends) { friend ->
                    FriendItem(
                        friend = friend,
                        toggleFollowingFor = { toggleFollowingFor(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun FriendItem(
    modifier: Modifier = Modifier,
    friend: Friend,
    toggleFollowingFor: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = friend.user.id)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = friend.user.about)
            }
            val followContentDescription = stringResource(R.string.followFriend, friend.user.id)
            val unfollowContentDescription = stringResource(R.string.unfollowFriend, friend.user.id)
            OutlinedButton(
                modifier = Modifier.semantics {
                    contentDescription =
                        if (friend.isFollowee) unfollowContentDescription else followContentDescription
                },
                onClick = { toggleFollowingFor(friend.user.id) }
            ) {
                val resource = if (friend.isFollowee) R.string.unfollow else R.string.follow
                Text(text = stringResource(resource))
            }
        }
    }
}
