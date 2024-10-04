package dev.m13d.somenet.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.ui.component.ScreenTitle

@Composable
fun FriendsScreen() {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(titleResource = R.string.friends)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.emptyFriendsMessage))
        }
    }
}
