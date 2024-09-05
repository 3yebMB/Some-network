package dev.m13d.somenet.timeline

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.m13d.somenet.R

@Composable
fun TimelineScreen() {
    Text(text = stringResource(id = R.string.timeline))
}
