package dev.m13d.somenet.utils

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dev.m13d.somenet.MainActivity

typealias ComposeTestRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
