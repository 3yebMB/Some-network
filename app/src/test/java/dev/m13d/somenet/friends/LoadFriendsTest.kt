package dev.m13d.somenet.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.user.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadFriendsTest {

    @Test
    fun noFriendsExisted() {
        val viewModel = FriendsViewModel()

        viewModel.loadFriends("samId")

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
    }

    class FriendsViewModel {
        private val _friendsState = MutableLiveData<FriendsState>()
        val friendsState: LiveData<FriendsState> = _friendsState
        fun loadFriends(userId: String) {
            _friendsState.value = FriendsState.Loaded(emptyList())
        }
    }

    sealed class FriendsState {

        data class Loaded(val friends: List<Friend>) : FriendsState()

        data class Friend(
            val user: User,
            val isFollow: Boolean,
        )
    }
}
