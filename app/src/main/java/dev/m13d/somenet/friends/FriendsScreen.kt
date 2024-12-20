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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.friends.states.FriendsScreenState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.PullToRefreshBox
import dev.m13d.somenet.ui.component.ScreenTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun FriendsScreen(userId: String) {

    val friendsViewModel = koinViewModel<FriendsViewModel>()
    val screenState = friendsViewModel.screenState.observeAsState().value ?: FriendsScreenState()

    LaunchedEffect(key1 = userId, block = { friendsViewModel.loadFriends(userId)})
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
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(titleResource = R.string.friends)
            Spacer(modifier = Modifier.height(16.dp))
            FriendsList(
                isRefreshing = screenState.isLoading,
                friends = screenState.friends,
                currentlyUpdatingFriends = screenState.updatingFriends,
                onRefresh = { onRefresh() },
                toggleFollowingFor = { toggleFollowingFor(it) }
            )
        }
        InfoMessage(stringResource = screenState.error)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FriendsList(
    isRefreshing: Boolean,
    friends: List<Friend>,
    currentlyUpdatingFriends: List<String>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    toggleFollowingFor: (String) -> Unit,
) {
    val description = stringResource(R.string.loading)

    PullToRefreshBox(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = description },
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() },
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
                        isTogglingFriendship = friend.user.id in currentlyUpdatingFriends,
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
    isTogglingFriendship: Boolean,
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
            val updatingDescription = stringResource(R.string.updatingFriendship, friend.user.id)
            OutlinedButton(
                modifier = Modifier.semantics {
                    contentDescription = if (friend.isFollowee) unfollowContentDescription else followContentDescription
                },
                enabled = !isTogglingFriendship,
                onClick = { toggleFollowingFor(friend.user.id) }
            ) {
                if (isTogglingFriendship) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp)
                            .semantics { contentDescription = updatingDescription },
                        strokeWidth = 2.dp,
                    )
                } else {
                    val resource = if (friend.isFollowee) R.string.unfollow else R.string.follow
                    Text(text = stringResource(resource))
                }
            }
        }
    }
}
